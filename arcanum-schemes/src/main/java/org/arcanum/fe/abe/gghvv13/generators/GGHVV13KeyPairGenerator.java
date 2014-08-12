package org.arcanum.fe.abe.gghvv13.generators;

import org.arcanum.Element;
import org.arcanum.common.fe.generator.KeyPairGenerator;
import org.arcanum.fe.abe.gghvv13.params.GGHVV13MasterSecretKeyParameters;
import org.arcanum.fe.abe.gghvv13.params.GGHVV13Parameters;
import org.arcanum.fe.abe.gghvv13.params.GGHVV13PublicKeyParameters;
import org.arcanum.pairing.Pairing;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHVV13KeyPairGenerator extends KeyPairGenerator<GGHVV13Parameters> {

    public AsymmetricCipherKeyPair generateKeyPair() {
        GGHVV13Parameters parameters = params.getParameters();

        Pairing pairing = parameters.getPairing();

        // Sample secret key
        Element alpha = pairing.getZr().newRandomElement().getImmutable();

        // Sample public key
        int n = parameters.getN();
        Element[] hs = new Element[n];
        for (int i = 0; i < hs.length; i++)
            hs[i] = pairing.getFieldAt(1).newRandomElement().getImmutable();

        Element H = pairing.getFieldAt(pairing.getDegree()).newElement().powZn(alpha).getImmutable();

        // Return the keypair
        return new AsymmetricCipherKeyPair(
                new GGHVV13PublicKeyParameters(parameters, H, hs),
                new GGHVV13MasterSecretKeyParameters(parameters, alpha)
        );
    }


}
