package org.arcanum.signature.ps06.params;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PS06SignParameters extends PS06KeyParameters {
    private PS06SecretKeyParameters secretKey;


    public PS06SignParameters(PS06SecretKeyParameters secretKey) {
        super(true, secretKey.getParameters());

        this.secretKey = secretKey;
    }

    public PS06SecretKeyParameters getSecretKey() {
        return secretKey;
    }

}
