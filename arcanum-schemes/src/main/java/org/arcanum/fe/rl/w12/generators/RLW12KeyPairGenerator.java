package org.arcanum.fe.rl.w12.generators;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.common.fe.generator.KeyPairGenerator;
import org.arcanum.fe.rl.w12.params.RLW12MasterSecretKeyParameters;
import org.arcanum.fe.rl.w12.params.RLW12Parameters;
import org.arcanum.fe.rl.w12.params.RLW12PublicKeyParameters;
import org.arcanum.field.util.ElementUtils;
import org.arcanum.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class RLW12KeyPairGenerator extends KeyPairGenerator<RLW12Parameters> {

    public AsymmetricCipherKeyPair generateKeyPair() {
        RLW12Parameters parameters = params.getParameters();

        Pairing pairing = PairingFactory.getPairing(parameters.getParameters());
        Element g = parameters.getG();

        // Generate required elements
        Element z = ElementUtils.randomIn(pairing, g).getImmutable();
        Element hStart = ElementUtils.randomIn(pairing, g).getImmutable();
        Element hEnd = ElementUtils.randomIn(pairing, g).getImmutable();

        Element[] hs = new Element[parameters.getAlphabetSize()];
        for (int i = 0; i < hs.length; i++) {
            hs[i] = ElementUtils.randomIn(pairing, g).getImmutable();
        }

        Element alpha = pairing.getZr().newRandomElement().getImmutable();
        Element omega = pairing.pairing(g, g).powZn(alpha).getImmutable();

        // Return the keypair

        return new AsymmetricCipherKeyPair(
                new RLW12PublicKeyParameters(parameters, z, hStart, hEnd, hs, omega),
                new RLW12MasterSecretKeyParameters(parameters, alpha)
        );
    }


}
