package org.arcanum.fe.abe.bns14.generators;

import org.arcanum.fe.abe.bns14.params.BNS14Parameters;
import org.arcanum.sampler.UniformOneMinusOneSampler;
import org.arcanum.trapdoor.mp12.utils.MP12P2EngineFactory;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14ParametersGenerator {
    private SecureRandom random;

    private int ell;
    private int depth;
    private int n;
    private int k;


    public BNS14ParametersGenerator(SecureRandom random, int ell, int depth) {
        this.random = random;
        this.ell = ell;
        this.depth = depth;

        this.n = 4;
        this.k = 64;
    }


    public BNS14Parameters generateParameters() {
        return new BNS14Parameters(
                random,
                ell,
                n,
                new MP12P2EngineFactory(random, n, k),
                MP12P2Utils.getLWENoiseSampler(random, n),
                new UniformOneMinusOneSampler(random)
        );
    }
}
