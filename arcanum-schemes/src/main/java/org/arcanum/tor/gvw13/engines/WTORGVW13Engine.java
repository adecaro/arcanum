package org.arcanum.tor.gvw13.engines;

import org.arcanum.Element;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.pairing.Pairing;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.tor.gvw13.params.WTORGVW13KeyParameters;
import org.arcanum.tor.gvw13.params.WTORGVW13PublicKeyParameters;
import org.arcanum.tor.gvw13.params.WTORGVW13RecodeParameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class WTORGVW13Engine extends AbstractElementCipher {

    private CipherParameters param;
    private Pairing pairing;

    public ElementCipher init(CipherParameters param) {
        this.param = param;

        WTORGVW13KeyParameters keyParameters = (WTORGVW13KeyParameters) param;
        pairing = PairingFactory.getPairing(keyParameters.getParameters().getParameters());

        return this;
    }


    public Element processElements(Element... input) {
        if (param instanceof WTORGVW13PublicKeyParameters) {
            WTORGVW13PublicKeyParameters keyParameters = (WTORGVW13PublicKeyParameters) param;

            // Read Input
            Element s = input[0];

            // Encode
            Element result;
            if (keyParameters.getLevel() == 0) {
                result = keyParameters.getLeft().powZn(s);
            } else {
                result = pairing.pairing(
                        keyParameters.getParameters().getG1a(),
                        keyParameters.getRight()
                ).powZn(s);
            }

            return result;
        } else {
            WTORGVW13RecodeParameters keyParameters = (WTORGVW13RecodeParameters) param;

            // Read Input
            Element c0 = input[0];
            Element c1 = input[1];

            // Recode
            return pairing.pairing(c0, keyParameters.getRk()).mul(c1);
       }

    }
}
