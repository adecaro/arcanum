package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Element;
/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2PublicKeyParameters extends MP12HLPublicKeyParameters {

    private MP12PLP2PublicKeyParameters publicKeyParameters;

    public MP12HLP2PublicKeyParameters(MP12Parameters parameters,
                                       MP12PLP2PublicKeyParameters primitiveLatticPk,
                                       Element A, int m) {
        super(parameters, primitiveLatticPk, A, m);

        this.publicKeyParameters = primitiveLatticPk;
    }

    public MP12PLP2PublicKeyParameters getPrimitiveLatticPk() {
        return publicKeyParameters;
    }
}
