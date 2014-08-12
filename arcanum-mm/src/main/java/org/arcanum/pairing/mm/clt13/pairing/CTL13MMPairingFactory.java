package org.arcanum.pairing.mm.clt13.pairing;

import org.arcanum.common.parameters.Parameters;
import org.arcanum.pairing.Pairing;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class CTL13MMPairingFactory {

    public static Pairing getPairing(SecureRandom random, Parameters parameters) {
        return new CTL13MMPairing(random, parameters);
    }

}
