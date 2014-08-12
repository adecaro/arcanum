package org.arcanum.fe.abe.gvw13.generators;

import org.arcanum.common.cipher.generators.ElementKeyPairGenerator;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.cipher.params.ElementKeyPairParameters;
import org.arcanum.common.fe.generator.KeyPairGenerator;
import org.arcanum.fe.abe.gvw13.params.GVW13MasterSecretKeyParameters;
import org.arcanum.fe.abe.gvw13.params.GVW13Parameters;
import org.arcanum.fe.abe.gvw13.params.GVW13PublicKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13KeyPairGenerator extends KeyPairGenerator<GVW13Parameters> {

    public AsymmetricCipherKeyPair generateKeyPair() {
        GVW13Parameters parameters = params.getParameters();

        ElementKeyPairGenerator tor = parameters.getTorKeyPairGenerater();
        int ell = parameters.getEll();

        ElementCipherParameters[] publicKeys = new ElementCipherParameters[ell * 2 + 1];
        ElementCipherParameters[] secretKeys = new ElementCipherParameters[ell * 2 + 1];

        for (int i = 0, size = 2 * ell; i < size; i++) {
            ElementKeyPairParameters keyPair = tor.generateKeyPair();
            publicKeys[i] = keyPair.getPublic();
            secretKeys[i] = keyPair.getPrivate();
        }

        ElementKeyPairParameters keyPair = tor.generateKeyPair();
        publicKeys[2 * ell] = keyPair.getPublic();
        secretKeys[2 * ell] = keyPair.getPrivate();

        // Return the keypair
        return new AsymmetricCipherKeyPair(
                new GVW13PublicKeyParameters(parameters, publicKeys),
                new GVW13MasterSecretKeyParameters(parameters, secretKeys)
        );
    }

}
