package org.arcanum.fe.rl.w12.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class RLW12KeyParameters extends AsymmetricKeyParameter {

    private RLW12Parameters parameters;


    public RLW12KeyParameters(boolean isPrivate, RLW12Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public RLW12Parameters getParameters() {
        return parameters;
    }
}


