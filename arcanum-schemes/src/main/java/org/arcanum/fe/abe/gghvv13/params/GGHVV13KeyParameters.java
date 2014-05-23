package org.arcanum.fe.abe.gghvv13.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHVV13KeyParameters extends AsymmetricKeyParameter {

    private GGHVV13Parameters parameters;


    public GGHVV13KeyParameters(boolean isPrivate, GGHVV13Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public GGHVV13Parameters getParameters() {
        return parameters;
    }
}


