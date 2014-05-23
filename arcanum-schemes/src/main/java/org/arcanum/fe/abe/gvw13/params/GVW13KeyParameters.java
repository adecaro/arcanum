package org.arcanum.fe.abe.gvw13.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13KeyParameters extends AsymmetricKeyParameter {

    private GVW13Parameters parameters;

    public GVW13KeyParameters(boolean isPrivate, GVW13Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public GVW13Parameters getParameters() {
        return parameters;
    }

}


