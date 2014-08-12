package org.arcanum.tor.gvw13.params;

import org.arcanum.Field;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.common.cipher.params.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TORGVW13PublicKeyParameters extends TORGVW13KeyParameters {

    private ElementCipherParameters publicKeyParameters;
    private ElementCipher owf;
    private ElementCipher otp;
    private Field owfInputField, owfOutputField;

    public TORGVW13PublicKeyParameters(TORGVW13Parameters parameters,
                                       ElementCipherParameters publicKeyParameters,
                                       ElementCipher owf,
                                       Field owfInputField,
                                       Field owfOutputField,
                                       ElementCipher otp) {
        super(false, parameters);

        this.publicKeyParameters = publicKeyParameters;
        this.owf = owf;
        this.owfInputField = owfInputField;
        this.owfOutputField = owfOutputField;
        this.otp = otp;
    }

    public ElementCipherParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public ElementCipher getOwf() {
        return owf;
    }

    public Field getOwfInputField() {
        return owfInputField;
    }

    public Field getOwfOutputField() {
        return owfOutputField;
    }

    public ElementCipher getOtp() {
        return otp;
    }
}
