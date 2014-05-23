package org.arcanum.fe.abe.bns14.generators;

import org.arcanum.Element;
import org.arcanum.fe.abe.bns14.params.BNS14KeyPairGenerationParameters;
import org.arcanum.fe.abe.bns14.params.BNS14MasterSecretKeyParameters;
import org.arcanum.fe.abe.bns14.params.BNS14Parameters;
import org.arcanum.fe.abe.bns14.params.BNS14PublicKeyParameters;
import org.arcanum.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.generators.MP12PLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.*;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private BNS14KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (BNS14KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        BNS14Parameters parameters = params.getParameters();

        // Generate trapdoor
        MP12HLP2KeyPairGenerator gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(
                parameters.getRandom(),
                parameters.getN(),
                parameters.getK()
        ));
        ElementKeyPairParameters keyPair = gen.generateKeyPair();
        MP12HLP2PublicKeyParameters latticePk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();

        // Generate primitive trapdoor
        MP12PLP2KeyPairGenerator primitiveGen = new MP12PLP2KeyPairGenerator();
        primitiveGen.init(new MP12PLP2KeyPairGenerationParameters(
                parameters.getRandom(),
                parameters.getN(),
                parameters.getK(),
                latticePk.getM() - (parameters.getN() * parameters.getK())
        ));
        ElementKeyPairParameters primitiveKeyPair = primitiveGen.generateKeyPair();
        MP12PLP2PublicKeyParameters primitiveLatticePk = (MP12PLP2PublicKeyParameters) primitiveKeyPair.getPublic();

        // generate public matrices
        Element D = latticePk.getA().getField().newRandomElement();

        Element[] Bs = new Element[parameters.getEll()];
        for (int i = 0, ell = parameters.getEll(); i < ell; i++) {
            Bs[i] = latticePk.getA().getField().newRandomElement();
        }

        // Return the keypair
        return new AsymmetricCipherKeyPair(
                new BNS14PublicKeyParameters(parameters, latticePk, primitiveLatticePk, D, Bs),
                new BNS14MasterSecretKeyParameters(
                        parameters,
                        (MP12HLP2PrivateKeyParameters) keyPair.getPrivate()
                )
        );
    }


}
