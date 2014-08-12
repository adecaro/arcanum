package org.arcanum.common.fe.params;

import org.arcanum.program.Program;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class SecretKeyGenerationParameters<MPK extends CipherParameters, MSK extends CipherParameters,P extends Program>
        extends KeyGenerationParameters {

    private MPK mpk;
    private MSK msk;
    private P program;

    public SecretKeyGenerationParameters(AsymmetricCipherKeyPair keyPair, P program) {
        this((MPK) keyPair.getPublic(), (MSK) keyPair.getPrivate(), program);
    }

    public SecretKeyGenerationParameters(MPK mpk, MSK msk, P program) {
        super(null, 0);

        this.mpk = mpk;
        this.msk = msk;
        this.program = program;
    }

    public MPK getMpk() {
        return mpk;
    }

    public MSK getMsk() {
        return msk;
    }

    public P getProgram() {
        return program;
    }
}
