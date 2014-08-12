package org.arcanum.tor.gvw13.generators;

import org.arcanum.Element;
import org.arcanum.pairing.Pairing;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.tor.gvw13.params.WTORGVW13KeyPairGenerationParameters;
import org.arcanum.tor.gvw13.params.WTORGVW13Parameters;
import org.arcanum.tor.gvw13.params.WTORGVW13PublicKeyParameters;
import org.arcanum.tor.gvw13.params.WTORGVW13SecretKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class WTORGVW13KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private WTORGVW13KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (WTORGVW13KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        WTORGVW13Parameters parameters = params.getParameters();
        int level = params.getLevel();

        Pairing pairing = PairingFactory.getPairing(parameters.getParameters());

        if (level == 1) {
            Element t = pairing.getZr().newRandomElement().getImmutable();
            Element tInv = t.invert();

            Element left = parameters.getG1a().powZn(tInv);
            Element right = parameters.getG2a().powZn(tInv);

            return new AsymmetricCipherKeyPair(
                    new WTORGVW13PublicKeyParameters(parameters, left, right, level),
                    new WTORGVW13SecretKeyParameters(parameters, t)
            );
        } else {
            Element right = pairing.getG2().newRandomElement();

            return new AsymmetricCipherKeyPair(
                    new WTORGVW13PublicKeyParameters(parameters, null, right, level),
                    null
            );
        }
    }


}
