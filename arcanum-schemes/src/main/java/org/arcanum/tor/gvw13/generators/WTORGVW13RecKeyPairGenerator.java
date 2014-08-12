package org.arcanum.tor.gvw13.generators;

import org.arcanum.Element;
import org.arcanum.common.cipher.CipherParametersGenerator;
import org.arcanum.tor.gvw13.params.WTORGVW13ReKeyPairGenerationParameters;
import org.arcanum.tor.gvw13.params.WTORGVW13RecodeParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class WTORGVW13RecKeyPairGenerator implements CipherParametersGenerator {
    private WTORGVW13ReKeyPairGenerationParameters params;


    public CipherParametersGenerator init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (WTORGVW13ReKeyPairGenerationParameters) keyGenerationParameters;

        return this;
    }

    public CipherParameters generateKey() {
        Element rk = params.getTargetPk().getRight().mul(
                params.getRightPk().getRight()
        ).powZn(params.getLeftSk().getT()).getImmutable();

        return new WTORGVW13RecodeParameters(params.getParameters(), rk);
    }


}
