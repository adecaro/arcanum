package org.arcanum.fe.ibe.dip10.params;

import org.arcanum.common.parameters.Parameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AHIBEDIP10KeyParameters extends AsymmetricKeyParameter {

    private Parameters parameters;


    public AHIBEDIP10KeyParameters(boolean isPrivate, Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public Parameters getParameters() {
        return parameters;
    }

}

