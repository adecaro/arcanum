package org.arcanum.trapdoor.mp12.params;

import org.arcanum.util.cipher.params.ElementKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12KeyParameters extends ElementKeyParameter {

    private MP12Parameters parameters;


    public MP12KeyParameters(boolean isPrivate, MP12Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public MP12Parameters getParameters() {
        return parameters;
    }

}


