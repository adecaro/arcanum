package org.arcanum.fe.abe.gvw13.params;

import org.arcanum.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13MasterSecretKeyParameters extends GVW13KeyParameters {

    private ElementCipherParameters[] torPrivateKeys;


    public GVW13MasterSecretKeyParameters(GVW13Parameters parameters, ElementCipherParameters[] torPrivateKeys) {
        super(true, parameters);

        this.torPrivateKeys = torPrivateKeys;
    }


    public ElementCipherParameters getCipherParametersAt(int index, int b) {
        return torPrivateKeys[index * 2 + b];
    }
}
