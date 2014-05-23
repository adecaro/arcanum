package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Matrix;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2PrivateKeyParameters extends MP12KeyParameters {

    protected Matrix R;


    public MP12HLP2PrivateKeyParameters(MP12Parameters parameters, Matrix R) {
        super(true, parameters);
        this.R = R;
    }

    public Matrix getR() {
        return R;
    }
}
