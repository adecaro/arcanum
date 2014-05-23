package org.arcanum.tor.gvw13.params;

import org.arcanum.util.cipher.params.ElementKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TORGVW13KeyParameters extends ElementKeyParameter {

    private TORGVW13Parameters parameters;


    public TORGVW13KeyParameters(boolean isPrivate, TORGVW13Parameters parameters) {
        super(isPrivate);

        this.parameters = parameters;
    }


    public TORGVW13Parameters getParameters() {
        return parameters;
    }
}


