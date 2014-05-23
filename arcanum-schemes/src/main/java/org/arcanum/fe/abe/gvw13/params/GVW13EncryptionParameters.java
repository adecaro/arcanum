package org.arcanum.fe.abe.gvw13.params;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13EncryptionParameters extends GVW13KeyParameters {

    private GVW13PublicKeyParameters publicKey;
    private String assignment;


    public GVW13EncryptionParameters(GVW13PublicKeyParameters publicKey,
                                     String assignment) {
        super(false, publicKey.getParameters());

        this.publicKey = publicKey;
        this.assignment = assignment;
    }


    public GVW13PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public String getAssignment() {
        return assignment;
    }
}
