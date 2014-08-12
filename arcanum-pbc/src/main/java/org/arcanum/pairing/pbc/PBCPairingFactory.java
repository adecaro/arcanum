package org.arcanum.pairing.pbc;

import org.arcanum.common.parameters.Parameters;
import org.arcanum.pairing.Pairing;
import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCPairingFactory {

    public static boolean isPBCAvailable() {
        return WrapperLibraryProvider.isAvailable();
    }

    public static Pairing getPairing(Parameters parameters) {
        if (WrapperLibraryProvider.isAvailable())
            return new PBCPairing(parameters);
        else
            return null;
    }

}
