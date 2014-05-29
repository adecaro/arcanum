package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.*;
import org.arcanum.field.util.ElementUtils;
import org.arcanum.field.vector.VectorField;
import org.arcanum.sampler.SamplerFactory;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2LeftSampler extends MP12HLP2Sampler {

    protected Sampler<BigInteger> sampler;

    public ElementCipher init(ElementCipherParameters param) {
        super.init(param);

        // TODO: set the right parameter
        this.sampler = SamplerFactory.getInstance().getDiscreteGaussianSampler(
                parameters.getParameters().getRandom(),
                MP12P2Utils.TWICE_RRP
        );

        return this;
    }

    public Element processElements(Element... input) {
        Matrix M = (Matrix) input[0];
        Element u = input[1].duplicate();

        //  1. sample a random vector e2 ∈ Zm1 distributed statistically close to DZm1,σ ,
        Element e2 = new VectorField<Field>(
                parameters.getParameters().getRandom(),
                M.getTargetField(),
                M.getM()
        ).newElementFromSampler(sampler);

        //  2. run e1 ←R SamplePre(A,TA,y,σ) where y=u−(M1·e2)∈Znq,
        //     note that Λyq (A) is not empty since A is rank n,
        Element y = u.sub(M.mul(e2));
        Element e1 = super.processElements(y);

        //  3. output e ← (e1, e2) ∈ Zm+m1
        return ElementUtils.union(e1, e2);
    }


}
