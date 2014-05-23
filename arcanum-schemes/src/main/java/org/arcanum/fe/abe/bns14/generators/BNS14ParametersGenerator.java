package org.arcanum.fe.abe.bns14.generators;

import org.arcanum.fe.abe.bns14.params.BNS14Parameters;
import org.arcanum.sampler.UniformOneMinusOneSampler;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14ParametersGenerator {
    private SecureRandom random;
    private int ell;
    private int depth;


    public BNS14ParametersGenerator(SecureRandom random, int ell, int depth) {
        this.random = random;
        this.ell = ell;
        this.depth = depth;
    }


    public BNS14Parameters generateParameters() {
        int n = 4;
        return new BNS14Parameters(
                random,
                ell,
                n,
                64,
                MP12P2Utils.getLWENoiseSampler(random, n),
                new UniformOneMinusOneSampler(random)
        );
    }
}
