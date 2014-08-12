package org.arcanum.fe;

import org.arcanum.common.fe.generator.SecretKeyGenerator;
import org.arcanum.common.fe.params.EncryptionParameters;
import org.arcanum.common.fe.params.KeyPairGenerationParameters;
import org.arcanum.common.kem.KeyEncapsulationMechanism;
import org.arcanum.common.kem.KeyEncapsulationMechanism.Pair;
import org.arcanum.program.Assignment;
import org.arcanum.program.Program;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.junit.Before;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractKEMEngineTest<P extends Program> {

    protected SecureRandom random;
    protected KeyEncapsulationMechanism engine;
    protected AsymmetricCipherKeyPairGenerator keyPairGenerator;
    protected SecretKeyGenerator secretKeyGenerator;
    protected AsymmetricCipherKeyPair keyPair;
    protected CipherParameters params;


    @Before
    public void before() throws Exception {
        this.random = SecureRandom.getInstance("SHA1PRNG");

        this.engine = createEngine();
        this.secretKeyGenerator = createSecretKeyGenerator();
        this.keyPairGenerator = createkeyPairGenerator();
        this.params = generateParams();
    }


    protected abstract KeyEncapsulationMechanism createEngine();

    protected abstract SecretKeyGenerator createSecretKeyGenerator();

    protected abstract AsymmetricCipherKeyPairGenerator createkeyPairGenerator();

    protected abstract CipherParameters generateParams();


    protected void setup() {
        keyPairGenerator.init(new KeyPairGenerationParameters(random, params));
        keyPair = keyPairGenerator.generateKeyPair();
    }

    protected Pair encaps(Assignment assignment) {
        return engine
                .initForEncryption(new EncryptionParameters(keyPair.getPublic(), assignment))
                .process();
    }

    protected byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        return engine.initForDecryption(secretKey).processBlock(ciphertext);
    }

    protected CipherParameters keyGen(P program) {
        return secretKeyGenerator.init(keyPair).generateKey(program);
    }

}
