package org.arcanum.fe.abe.gvw13.params;

import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.fe.params.KeyParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13MasterSecretKeyParameters extends KeyParameters<GVW13Parameters> {

    private ElementCipherParameters[] torPrivateKeys;


    public GVW13MasterSecretKeyParameters(GVW13Parameters parameters, ElementCipherParameters[] torPrivateKeys) {
        super(true, parameters);

        this.torPrivateKeys = torPrivateKeys;
    }


    public ElementCipherParameters getCipherParametersAt(int index, int b) {
        return torPrivateKeys[index * 2 + b];
    }
}
