package org.arcanum.signature.ps06.generators;

import org.arcanum.Element;
import org.arcanum.pairing.Pairing;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.signature.ps06.params.PS06MasterSecretKeyParameters;
import org.arcanum.signature.ps06.params.PS06Parameters;
import org.arcanum.signature.ps06.params.PS06PublicKeyParameters;
import org.arcanum.signature.ps06.params.PS06SetupGenerationParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PS06SetupGenerator implements AsymmetricCipherKeyPairGenerator {
    private PS06SetupGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (PS06SetupGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        PS06Parameters parameters = param.getParameters();

        // take params
        Pairing pairing = PairingFactory.getPairing(parameters.getCurveParams());
        Element g = parameters.getG();
        int nU = parameters.getnU();
        int nM = parameters.getnM();

        // compute pk
        Element alpha = pairing.getZr().newRandomElement();
        Element g1 = g.powZn(alpha).getImmutable();
        Element g2 = pairing.getG1().newRandomElement().getImmutable();

        Element uPrime = pairing.getG1().newRandomElement().getImmutable();
        Element mPrime = pairing.getG1().newRandomElement().getImmutable();

        Element[] Us = new Element[nU];
        for (int i = 0; i < Us.length; i++) {
            Us[i] = pairing.getG1().newRandomElement().getImmutable();
        }

        Element[] Ms = new Element[nM];
        for (int i = 0; i < Ms.length; i++) {
            Ms[i] = pairing.getG1().newRandomElement().getImmutable();
        }

        // compute mks
        Element msk = g2.powZn(alpha).getImmutable();

        return new AsymmetricCipherKeyPair(
            new PS06PublicKeyParameters(parameters, g1, g2, uPrime, mPrime, Us, Ms),
            new PS06MasterSecretKeyParameters(parameters, msk)
        );
    }

}