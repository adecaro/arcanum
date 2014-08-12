package org.arcanum.signature.bls01.generators;

import org.arcanum.Element;
import org.arcanum.pairing.Pairing;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.signature.bls01.params.BLS01KeyGenerationParameters;
import org.arcanum.signature.bls01.params.BLS01Parameters;
import org.arcanum.signature.bls01.params.BLS01PrivateKeyParameters;
import org.arcanum.signature.bls01.params.BLS01PublicKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BLS01KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private BLS01KeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (BLS01KeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        BLS01Parameters parameters = param.getParameters();

        Pairing pairing = PairingFactory.getPairing(parameters.getParameters());
        Element g = parameters.getG();

        // Generate the secret key
        Element sk = pairing.getZr().newRandomElement();

        // Generate the corresponding public key
        Element pk = g.powZn(sk);

        return new AsymmetricCipherKeyPair(
            new BLS01PublicKeyParameters(parameters, pk.getImmutable()),
            new BLS01PrivateKeyParameters(parameters, sk.getImmutable())
        );
    }

}