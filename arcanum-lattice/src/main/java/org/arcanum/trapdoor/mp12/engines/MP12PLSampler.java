package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.*;
import org.arcanum.field.vector.VectorField;
import org.arcanum.sampler.SamplerFactory;
import org.arcanum.trapdoor.mp12.params.MP12PLPublicKeyParameters;
import org.arcanum.util.cipher.engine.AbstractElementCipher;

import java.math.BigInteger;

import static org.arcanum.field.floating.ApfloatUtils.SQRT_FIVE;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLSampler extends AbstractElementCipher {

    protected MP12PLPublicKeyParameters parameters;
    protected int n, k;

    protected Sampler<BigInteger> sampler;
    protected Field vf;


    public ElementCipher init(ElementCipherParameters param) {
        this.parameters = (MP12PLPublicKeyParameters) param;

        this.n = parameters.getParameters().getN();
        this.k = parameters.getK();
        this.sampler = SamplerFactory.getInstance().getDiscreteGaussianSampler(
                parameters.getParameters().getRandom(),
                parameters.getRandomizedRoundingParameter().multiply(SQRT_FIVE)
        );
        vf = new VectorField(
                parameters.getParameters().getRandom(),
                parameters.getZq(),
                k
        );

        return this;
    }

    public Element processElements(Element... input) {
        Vector syndrome = (Vector) input[0];
        if (syndrome.getSize() != n)
            throw new IllegalArgumentException("Invalid syndrome length.");

        Vector r = parameters.getPreimageField().newElement();

        for (int i = 0, base = 0; i < n; i++, base += k) {
            r.setAt(base, sampleZk(syndrome.getAt(i).toBigInteger()));
        }

        return r;
    }

    public Element processElementsTo(Element to, Element... input) {
        throw new IllegalStateException("Not implemented yet!!!");
    }



    private Element sampleZk(BigInteger u) {
        Vector x;
//            System.out.println("u = " + u);
        while (true) {
            x = (Vector) vf.newElementFromSampler(sampler);
            Element uu = parameters.getPrimitiveVector().mul(x);

//            System.out.println("uu = " + uu);
            if (u.equals(uu.toBigInteger()))
                break;

        }
//        System.out.println("x = " + x);

        return x;
    }


}