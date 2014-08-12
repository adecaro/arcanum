package org.arcanum.trapdoor.mp12.params;

import org.arcanum.common.cipher.params.ElementKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2InverterParameters extends ElementKeyParameter {

    protected MP12HLP2PublicKeyParameters pk;
    protected MP12HLP2PrivateKeyParameters sk;


    public MP12HLP2InverterParameters(MP12HLP2PublicKeyParameters pk, MP12HLP2PrivateKeyParameters sk) {
        super(true);

        this.pk = pk;
        this.sk = sk;
    }

    public MP12HLP2PublicKeyParameters getPk() {
        return pk;
    }

    public MP12HLP2PrivateKeyParameters getSk() {
        return sk;
    }
}
