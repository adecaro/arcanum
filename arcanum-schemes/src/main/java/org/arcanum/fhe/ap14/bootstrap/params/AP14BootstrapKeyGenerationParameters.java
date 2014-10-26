package org.arcanum.fhe.ap14.bootstrap.params;


import org.arcanum.common.cipher.params.ElementKeyGenerationParameters;
import org.arcanum.fhe.ap14.field.AP14GSW14Field;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14BootstrapKeyGenerationParameters extends ElementKeyGenerationParameters {

    private AP14GSW14Field field;

    public AP14BootstrapKeyGenerationParameters(SecureRandom random, AP14GSW14Field field) {
        super(random);

        this.field = field;
    }

    public AP14GSW14Field getField() {
        return field;
    }
}
