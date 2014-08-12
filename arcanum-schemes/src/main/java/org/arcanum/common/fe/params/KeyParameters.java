package org.arcanum.common.fe.params;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class KeyParameters<P extends CipherParameters> extends AsymmetricKeyParameter {

    private P parameters;

    public KeyParameters(boolean isPrivate, P parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public P getParameters() {
        return parameters;
    }

}


