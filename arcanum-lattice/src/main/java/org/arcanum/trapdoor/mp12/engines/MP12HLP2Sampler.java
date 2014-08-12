package org.arcanum.trapdoor.mp12.engines;

import org.apfloat.Apfloat;
import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.Vector;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.cipher.params.ElementKeyPairParameters;
import org.arcanum.common.concurrent.PoolExecutor;
import org.arcanum.common.math.Cholesky;
import org.arcanum.field.floating.ApfloatUtils;
import org.arcanum.field.floating.FloatingField;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.TwoByRowMatrixElement;
import org.arcanum.sampler.DiscreteGaussianCOVSampler;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleParameters;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static org.apfloat.ApfloatMath.sqrt;
import static org.arcanum.field.floating.ApfloatUtils.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2Sampler extends MP12PLP2Sampler {

    protected static Map<ElementCipherParameters, Matrix> covs = new HashMap<ElementCipherParameters, Matrix>();


    protected MP12HLP2PublicKeyParameters pk;
    protected MP12HLP2PrivateKeyParameters sk;
    protected Element A;

    protected Sampler<? extends Element> perturbationSampler;
    protected int matrixExtensionLength;


    public MP12HLP2Sampler() {
        this.matrixExtensionLength = 0;
    }


    public ElementCipher init(ElementCipherParameters param) {
        ElementKeyPairParameters keyPair = ((MP12HLP2SampleParameters) param).getKeyPair();

        this.pk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();
        this.sk = (MP12HLP2PrivateKeyParameters) keyPair.getPrivate();
        this.A = pk.getA();

        // Init the Primitive Lattice Sampler
        super.init(pk);

        // Init offline sampler
        perturbationSampler = new DiscreteGaussianCOVSampler(
                sk.getParameters().getRandom(),
                computeCoviarianceMatrix(),
                sk.getR().getTargetField(),
                pk.getRandomizedRoundingParameter()
        );

        return this;
    }

    public Element processElements(Element... input) {
        // Offline phase
        // sample perturbation
        Element[] perturbation = samplePerturbation();
        Element p = perturbation[0];
        Element offset = perturbation[1];

        // Online phase
        Element u = input[0];

        // Compute syndrome w
        // offset.negateThenAdd(u)
        Element v = u.duplicate().sub(offset);

        // Compute x
        Element z2 = super.processElements(v);
        Element z1 = sk.getR().mul(z2);

        return ((Vector) p).add(z1, z2);
    }


    public void setMatrixExtensionLength(int matrixExtensionLength) {
        this.matrixExtensionLength = matrixExtensionLength;
    }

    protected Element[] samplePerturbation() {
        Element p = perturbationSampler.sample();
        Element offset = A.mul(p);

        return new Element[]{p, offset};
    }

    protected Matrix computeCoviarianceMatrix() {
        Matrix cov = covs.get(sk);
        if (cov == null) {
            cov = computeCovarianceMatrixInternal();
            covs.put(sk, cov);
        }
        return cov;
    }

    protected Matrix computeCovarianceMatrixInternal() {
        // Setup parameters: compute gaussian parameter s
        int n = sk.getR().getN();
        final int m = sk.getR().getM();
        SecureRandom random = sk.getParameters().getRandom();

        Apfloat rrp = pk.getRandomizedRoundingParameter();
        Apfloat rrpSquare = square(rrp);
        Apfloat tworrpSquare = rrpSquare.multiply(IFOUR);

        Apfloat lweNoisParameter = SQRT_TWO.multiply(
                ITWO.multiply(sqrt(newApfloat(n)))
        ).multiply(rrpSquare).multiply(rrp);

        Apfloat sq = square(
                lweNoisParameter.multiply(
                        ApfloatUtils.sqrt(n).add(ApfloatUtils.sqrt(m)).add(ApfloatUtils.IONE)
                ).divide(SQRT_2PI)
        ).add(IONE).multiply(ISIX).multiply(rrpSquare);

        if (matrixExtensionLength > 0)
            n += matrixExtensionLength;
        FloatingField ff = new FloatingField(random);
        final MatrixField<FloatingField> mff = new MatrixField<FloatingField>(random, ff, n + m);

        Element sSquare = ff.newElement(sq);
        Element rSquare = ff.newElement(tworrpSquare);
        Element aSquare = ff.newElement(rrpSquare);

        Element b = sSquare.duplicate().sub(rSquare).sub(aSquare);
        final Element sqrtB = b.duplicate().sqrt();
        final Element rSquarePlusOneOverB = rSquare.duplicate().add(b.duplicate().invert());
        final Element sSquareMinusASquare = sSquare.duplicate().sub(aSquare);
        final Element sqrtBInverse = sqrtB.duplicate().invert();

        // Compute covariance matrix COV
        final int finalN = n;

        PoolExecutor<Matrix> executor = new PoolExecutor<Matrix>();
        Future<Matrix> C = executor.submitFuture(new Callable<Matrix>() {
            public Matrix call() throws Exception {
                if (matrixExtensionLength > 0) {
                    return new TwoByRowMatrixElement(
                            mff.newMatrix(sk.getR(), new Matrix.Transformer() {
                                public void transform(int row, int col, Element e) {
                                    e.mul(sqrtBInverse);
                                }
                            }),
                            mff.newNullMatrix(matrixExtensionLength, m)
                    );

                } else
                    return mff.newMatrix(sk.getR(), new Matrix.Transformer() {
                        public void transform(int row, int col, Element e) {
                            e.mul(sqrtBInverse);
                        }
                    });
            }
        });
        Future<Matrix> D = executor.submitFuture(new Callable<Matrix>() {
            public Matrix call() throws Exception {
                Matrix rightBottomPart = mff.newSquareMatrix(finalN);
                sk.getR().mulByTransposeTo(rightBottomPart, 0, 0, new Matrix.Transformer() {
                    public void transform(int row, int col, Element e) {
                        e.mul(rSquarePlusOneOverB).negate();
                        if (row == col)
                            e.add(sSquareMinusASquare);
                    }
                });
                if (matrixExtensionLength > 0)
                    rightBottomPart.transformDiagonal(new Matrix.Transformer() {
                        public void transform(int row, int col, Element e) {
                            if (row >= (finalN - matrixExtensionLength))
                                e.add(sSquareMinusASquare);
                        }
                    });

                return rightBottomPart;
            }
        });

        Matrix cov;
        try {
            cov = mff.newTwoByTwoElement(
                    mff.newIdentityMatrix(m, sqrtB), mff.newNullMatrix(m, n),
                    C.get(), D.get()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*
        final Matrix cov = mff.newElement();
        new PoolExecutor().submit(new Runnable() {
            public void run() {
                cov.setIdentityAt(0, 0, m, sqrtB);
            }
        }).submit(new Runnable() {
            public void run() {
                cov.setMatrixAt(m, 0, sk.getR(), new Matrix.Transformer() {
                    public void transform(int row, int col, Element e) {
                        e.mul(sqrtBInverse);
                    }
                });
            }
        }).submit(new Runnable() {
                      public void run() {
                          sk.getR().mulByTransposeTo(cov, m, m, new Matrix.Transformer() {
                              public void transform(int row, int col, Element e) {
                                  e.mul(rSquarePlusOneOverB).negate();
                                  if (row == col)
                                      e.add(sSquareMinusASquare);
                              }
                          });
                          if (matrixExtensionLength > 0)
                              cov.transformDiagonal(new Matrix.Transformer() {
                                  public void transform(int row, int col, Element e) {
                                      if (row >= m + (finalN - matrixExtensionLength))
                                          e.add(sSquareMinusASquare);
                                  }
                              });
                      }
                  }
        ).awaitTermination();
           */

        // Compute Cholesky decomposition
        Cholesky.choleskyAt(cov, m, m);

        return cov;
    }


}
