package org.arcanum.fe.ibe.lw11.params;

import org.arcanum.Parameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class UHIBELW11KeyParameters extends AsymmetricKeyParameter {

    private Parameters parameters;


    public UHIBELW11KeyParameters(boolean isPrivate, Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public Parameters getParameters() {
        return parameters;
    }

}


