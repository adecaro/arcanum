package org.arcanum.tor.gvw13.generators;


import org.arcanum.ElementCipher;
import org.arcanum.Field;
import org.arcanum.tor.gvw13.params.TORGVW13KeyPairGenerationParameters;
import org.arcanum.tor.gvw13.params.TORGVW13PublicKeyParameters;
import org.arcanum.tor.gvw13.params.TORGVW13SecretKeyParameters;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2ErrorTolerantOneTimePad;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2OneWayFunction;
import org.arcanum.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.MP12HLP2KeyPairGenerationParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2OneWayFunctionParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.arcanum.util.cipher.generators.ElementKeyPairGenerator;
import org.arcanum.util.cipher.params.ElementKeyGenerationParameters;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TORGVW13KeyPairGenerator implements ElementKeyPairGenerator {

    private TORGVW13KeyPairGenerationParameters params;
    private MP12HLP2KeyPairGenerator gen;


    public void init(ElementKeyGenerationParameters keyGenerationParameters) {
        this.params = (TORGVW13KeyPairGenerationParameters) keyGenerationParameters;

        // Init Lattice generator
        // TODO: k must be chosen differently
        gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(
                params.getRandom(),
                params.getParameters().getN(),  // n
                64                              // k
        ));
    }

    public ElementKeyPairParameters generateKeyPair() {
        ElementKeyPairParameters keyPair = gen.generateKeyPair();

        // One-way function
        ElementCipher owf = new MP12HLP2OneWayFunction();
        MP12HLP2OneWayFunctionParameters owfParams = new MP12HLP2OneWayFunctionParameters(
                (MP12HLP2PublicKeyParameters) keyPair.getPublic()
        );
        owf.init(owfParams);

        // error-tolerant version of the one-time pad
        ElementCipher otp = new MP12HLP2ErrorTolerantOneTimePad();

        return new ElementKeyPairParameters(
                new TORGVW13PublicKeyParameters(
                        params.getParameters(),
                        keyPair.getPublic(),
                        owf,
                        gen.getInputField(),
                        gen.getOutputField(),
                        otp
                ),
                new TORGVW13SecretKeyParameters(
                        params.getParameters(),
                        keyPair.getPrivate(),
                        gen.getOutputField()
                )
        );
    }


    public int getKeyLengthInBytes() {
        return gen.getMInBytes();
    }

    public Field getOwfInputField() {
        return gen.getInputField();
    }
}
