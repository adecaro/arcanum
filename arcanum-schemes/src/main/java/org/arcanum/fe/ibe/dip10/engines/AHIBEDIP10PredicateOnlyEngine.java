package org.arcanum.fe.ibe.dip10.engines;

import org.arcanum.Element;
import org.arcanum.common.fe.engine.PredicateOnlyPairingAsymmetricBlockCipher;
import org.arcanum.common.io.PairingStreamReader;
import org.arcanum.fe.ibe.dip10.params.AHIBEDIP10EncryptionParameters;
import org.arcanum.fe.ibe.dip10.params.AHIBEDIP10KeyParameters;
import org.arcanum.fe.ibe.dip10.params.AHIBEDIP10PublicKeyParameters;
import org.arcanum.fe.ibe.dip10.params.AHIBEDIP10SecretKeyParameters;
import org.arcanum.field.util.ElementUtils;
import org.arcanum.pairing.PairingFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AHIBEDIP10PredicateOnlyEngine extends PredicateOnlyPairingAsymmetricBlockCipher {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof AHIBEDIP10EncryptionParameters))
                throw new IllegalArgumentException("AHIBEDIP10EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof AHIBEDIP10SecretKeyParameters))
                throw new IllegalArgumentException("AHIBEDIP10SecretKeyParameters are required for decryption.");
        }

        this.pairing = PairingFactory.getPairing(((AHIBEDIP10KeyParameters) key).getParameters());
        this.inBytes = 0;
        this.outBytes = pairing.getGT().getLengthInBytes() + (2 * pairing.getG1().getLengthInBytes());
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof AHIBEDIP10SecretKeyParameters) {
            // Convert bytes to Elements...

            Element[] Cs = new PairingStreamReader(pairing, in, inOff).readElements(3,1,1);

            // Run the test
            AHIBEDIP10SecretKeyParameters sk = (AHIBEDIP10SecretKeyParameters) key;
            Element M = Cs[0].mul(pairing.pairing(sk.getK12(), Cs[2]).mul(pairing.pairing(sk.getK11(), Cs[1]).invert()).invert());

            return new byte[]{(byte) (M.isOne() ? 1 : 0)};
        } else {
            // Encrypt
            AHIBEDIP10EncryptionParameters encParams = (AHIBEDIP10EncryptionParameters) key;
            AHIBEDIP10PublicKeyParameters pk = encParams.getPublicKey();

            // Encrypt the attributes
            Element s = pairing.getZr().newRandomElement();

            Element C0 = pk.getOmega().powZn(s);

            Element C1 = pairing.getG1().newOneElement();
            for (int i = 0; i < encParams.getLength(); i++) {
                C1.mul(pk.getUAt(i).powZn(encParams.getIdAt(i)));
            }
            C1.mul(pk.getT()).powZn(s).mul(ElementUtils.randomIn(pairing, pk.getY4()));

            Element C2 = pk.getY1().powZn(s).mul(ElementUtils.randomIn(pairing, pk.getY4()));

            // Convert the Elements to byte arrays
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
            try {
                bytes.write(C0.toBytes());
                bytes.write(C1.toBytes());
                bytes.write(C2.toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bytes.toByteArray();
        }
    }

}
