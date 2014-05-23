package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.Vector;
import org.arcanum.field.floating.FloatingField;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.sampler.DiscreteGaussianCOVSampler;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleParameters;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;
import org.arcanum.util.cipher.engine.ElementCipher;
import org.arcanum.util.cipher.params.ElementCipherParameters;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;
import org.arcanum.util.concurrent.PoolExecutor;
import org.arcanum.util.math.Cholesky;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static org.arcanum.trapdoor.mp12.utils.MP12P2Utils.getSSquare;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2Sampler extends MP12PLP2Sampler {

    protected static Map<ElementCipherParameters, Matrix> covs = new HashMap<ElementCipherParameters, Matrix>();


    protected MP12HLP2PublicKeyParameters pk;
    protected MP12HLP2PrivateKeyParameters sk;

    protected Sampler<Vector> offlineSampler;


    public ElementCipher init(ElementCipherParameters param) {
        ElementKeyPairParameters keyPair = ((MP12HLP2SampleParameters) param).getKeyPair();

        pk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();
        sk = (MP12HLP2PrivateKeyParameters) keyPair.getPrivate();

        // Init the Primitive Lattice Sampler
        super.init(pk);

        // Init offline sampler
        SecureRandom random = sk.getParameters().getRandom();
        Matrix cov = covs.get(sk);
        if (cov == null) {
            cov = computeCovarianceMatrix(random, 2 * n, n * k);
            covs.put(sk, cov);
        }

        offlineSampler = new DiscreteGaussianCOVSampler(random, cov, sk.getR().getTargetField(), MP12P2Utils.RRP);

        return this;

    }

    public Element processElements(Element... input) {
        // Online phase
        Element u = input[0];

        // sample perturbation
        Element[] perturbation = samplePerturbation();
        Element p = perturbation[0];
        Element offset = perturbation[1];

        // Compute syndrome w
        Element v = u.duplicate().sub(offset);

        // Compute x
        Element z2 = super.processElements(v);
        Element z1 = sk.getR().mul(z2);

        return ((Vector) p).add(z1, z2);
    }

    protected Element[] samplePerturbation() {
        Element p = offlineSampler.sample();
        Element o = pk.getA().mul(p);

        return new Element[]{p, o};
    }

    protected Matrix computeCovarianceMatrix(SecureRandom random, int n, final int m) {
        // Compute covariance matrix COV
        MatrixField<FloatingField> ff = new MatrixField<FloatingField>(random, new FloatingField(random), n + m);

        Element sSquare = ff.getTargetField().newElement(getSSquare(n, m));
        Element rSquare = ff.getTargetField().newElement(MP12P2Utils.TWO_RRP_SQUARE);
        Element aSquare = ff.getTargetField().newElement(MP12P2Utils.RRP_SQUARE);

        Element b = sSquare.duplicate().sub(rSquare).sub(aSquare);
        final Element sqrtB = b.duplicate().sqrt();
        final Element rSquarePlusOneOverB = rSquare.duplicate().add(b.duplicate().invert());
        final Element sSquareMinusASquare = sSquare.duplicate().sub(aSquare);

        final Element sqrtBInverse = sqrtB.duplicate().invert();

        final Matrix cov = ff.newElement();
        new PoolExecutor().submit(new Runnable() {
            public void run() {
                cov.setSubMatrixToIdentityAt(0, 0, m, sqrtB);
            }
        }).submit(new Runnable() {
            public void run() {
                cov.setSubMatrixFromMatrixAt(m, 0, sk.getR(), new Matrix.Transformer() {
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
                      }
                  }
        ).awaitTermination();

        // Compute Cholesky decomposition
        Cholesky.choleskyAt(cov, m, m);

        return cov;
    }

}
