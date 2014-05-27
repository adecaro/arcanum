package org.arcanum.fe.abe.gvw13.params;

import org.arcanum.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13PublicKeyParameters extends GVW13KeyParameters {

    private ElementCipherParameters[] torPublicKeys;

    public GVW13PublicKeyParameters(GVW13Parameters parameters, ElementCipherParameters[] torPublicKeys) {
        super(false, parameters);

        this.torPublicKeys = torPublicKeys;
    }

    public ElementCipherParameters getCipherParametersAt(int index, int b) {
        return torPublicKeys[index * 2 + b];
    }

    public ElementCipherParameters getCipherParametersAt(int index, boolean b) {
        return torPublicKeys[index * 2 + (b ? 1 : 0)];
    }

    public ElementCipherParameters getCipherParametersOut() {
        return torPublicKeys[torPublicKeys.length - 1];
    }
}
