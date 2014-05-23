package org.arcanum.fe.abe.bns14.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14KeyParameters extends AsymmetricKeyParameter {

    private BNS14Parameters parameters;

    public BNS14KeyParameters(boolean isPrivate, BNS14Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public BNS14Parameters getParameters() {
        return parameters;
    }

}


