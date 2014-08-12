package org.arcanum.common.kem;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface KeyEncapsulationMechanism extends AsymmetricBlockCipher {

    /**
     * initialise the cipher for encryption.
     *
     * @param param the key and other data required by the cipher.
     * @since 1.0.0
     */
    public KeyEncapsulationMechanism initForEncryption(CipherParameters param);

    /**
     * initialise the cipher for decryption.
     *
     * @param param the key and other data required by the cipher.
     * @since 1.0.0
     */
    public KeyEncapsulationMechanism initForDecryption(CipherParameters param);


    /**
     *
     * @return
     * @since 1.0.0
     */
    public int getKeyBlockSize();

    /**
     *
     * @return
     * @since 1.0.0
     */
    public byte[] processBlock(byte[] in);

    /**
     *
     * @return
     * @since 1.0.0
     */
    public Pair process();



    public static class Pair {
        private byte[] key;
        private byte[] encapsulation;

        public Pair(byte[] key, byte[] encapsulation) {
            this.key = key;
            this.encapsulation = encapsulation;
        }

        public byte[] getKey() {
            return key;
        }

        public byte[] getEncapsulation() {
            return encapsulation;
        }
    }
}
