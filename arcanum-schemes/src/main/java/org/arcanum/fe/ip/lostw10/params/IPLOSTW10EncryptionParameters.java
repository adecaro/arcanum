package org.arcanum.fe.ip.lostw10.params;

import org.arcanum.Element;
import org.arcanum.field.util.ElementUtils;

import java.util.Arrays;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class IPLOSTW10EncryptionParameters extends IPLOSTW10KeyParameters {

    private IPLOSTW10PublicKeyParameters publicKey;
    private Element[] x;


    public IPLOSTW10EncryptionParameters(IPLOSTW10PublicKeyParameters publicKey,
                                         Element[] x) {
        super(false, publicKey.getParameters());

        this.publicKey = publicKey;
        this.x = ElementUtils.cloneImmutable(x);
    }


    public IPLOSTW10PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public Element[] getX() {
        return Arrays.copyOf(x, x.length);
    }

    public Element getXAt(int index) {
        return x[index];
    }

    public int getLength() {
        return x.length;
    }
}
