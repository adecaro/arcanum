package org.arcanum.fe.ibe.dip10.params;

import org.arcanum.Element;
import org.arcanum.field.util.ElementUtils;

import java.util.Arrays;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AHIBEDIP10EncryptionParameters extends AHIBEDIP10KeyParameters {

    private AHIBEDIP10PublicKeyParameters publicKey;
    private Element[] ids;


    public AHIBEDIP10EncryptionParameters(AHIBEDIP10PublicKeyParameters publicKey,
                                          Element[] ids) {
        super(true, publicKey.getParameters());

        this.publicKey = publicKey;
        this.ids = ElementUtils.cloneImmutable(ids);
    }


    public AHIBEDIP10PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public Element[] getIds() {
        return Arrays.copyOf(ids, ids.length);
    }

    public Element getIdAt(int index) {
        return ids[index];
    }

    public int getLength() {
        return ids.length;
    }
}
