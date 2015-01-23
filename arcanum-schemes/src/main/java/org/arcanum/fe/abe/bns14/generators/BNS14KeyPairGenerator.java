package org.arcanum.fe.abe.bns14.generators;

import org.arcanum.Element;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.fe.generator.KeyPairGenerator;
import org.arcanum.fe.abe.bns14.params.BNS14MasterSecretKeyParameters;
import org.arcanum.fe.abe.bns14.params.BNS14Parameters;
import org.arcanum.fe.abe.bns14.params.BNS14PublicKeyParameters;
import org.arcanum.trapdoor.mp12.utils.MP12EngineFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14KeyPairGenerator extends KeyPairGenerator<BNS14Parameters> {

    public AsymmetricCipherKeyPair generateKeyPair() {
        BNS14Parameters parameters = params.getParameters();

        MP12EngineFactory factory = parameters.getFactory();
        ElementCipherParameters latticeSk = factory.init();

        // generate public matrices
        Element D = factory.getPublicField().newRandomElement();

        Element[] Bs = new Element[parameters.getEll()];
        for (int i = 0, ell = parameters.getEll(); i < ell; i++) {
            Bs[i] = factory.getPublicField().newRandomElement();
        }

        // Return the keypair
        return new AsymmetricCipherKeyPair(
                new BNS14PublicKeyParameters(
                        parameters,
                        D, Bs),
                new BNS14MasterSecretKeyParameters(
                        parameters,
                        latticeSk
                )
        );
    }

}
