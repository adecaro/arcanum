package org.arcanum.tor.gvw13.params;

import org.arcanum.Field;
import org.arcanum.common.cipher.params.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TORGVW13SecretKeyParameters extends TORGVW13KeyParameters {

    private ElementCipherParameters privateKeyParameter;
    private Field owfInputField;

    public TORGVW13SecretKeyParameters(TORGVW13Parameters parameters, ElementCipherParameters privateKeyParameters, Field owfInputField) {
        super(true, parameters);

        this.privateKeyParameter = privateKeyParameters;
        this.owfInputField = owfInputField;
    }

    public ElementCipherParameters getPrivateKeyParameter() {
        return privateKeyParameter;
    }

    public Field getOwfInputField() {
        return owfInputField;
    }
}
