package org.arcanum.fe.abe.gghsw13.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13KeyParameters extends AsymmetricKeyParameter {

    private GGHSW13Parameters parameters;


    public GGHSW13KeyParameters(boolean isPrivate, GGHSW13Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public GGHSW13Parameters getParameters() {
        return parameters;
    }
}


