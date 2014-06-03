package org.arcanum.fhe.ap14;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.trapdoor.mp12.engines.MP12PLP2MatrixSampler;
import org.arcanum.trapdoor.mp12.generators.MP12PLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.MP12PLP2KeyPairGenerationParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TestAP14 {

    public static void main(String[] args) {
        int n = 4;
        int k = 20;
        SecureRandom random = new SecureRandom();


        MP12PLP2KeyPairGenerator gen = new MP12PLP2KeyPairGenerator();
        gen.init(new MP12PLP2KeyPairGenerationParameters(random, n, k));
        MP12PLP2PublicKeyParameters pk = (MP12PLP2PublicKeyParameters) gen.generateKeyPair().getPublic();

        // Gen Secret Key
        Element s = pk.getPreimageField().newField(n - 1).newElementFromSampler(MP12P2Utils.getLWENoiseSampler(random, n));

        // Enc
        Element mu = pk.getZq().newOneElement();

        // sample error
        Element e = pk.getPreimageField().newElementFromSampler(MP12P2Utils.getLWENoiseSampler(random, n));

        // generate cts
        Matrix ct1 = (Matrix) pk.getG().getField().newElement();
        ct1.setRowsToRandom(0, ct1.getN() - 1)
                .getRowsViewAt(0, ct1.getN() - 1)
                .mulTo(s, ct1.getRowViewAt(ct1.getN() - 1));
        ct1.getRowViewAt(ct1.getN() - 1).negate().add(e);
        ct1.add(pk.getG().duplicate().mul(mu));

        Matrix ct2 = (Matrix) pk.getG().getField().newElement();
        ct2.setRowsToRandom(0, ct2.getN() - 1)
                .getRowsViewAt(0, ct2.getN() - 1)
                .mulTo(s, ct2.getRowViewAt(ct2.getN() - 1));
        ct2.getRowViewAt(ct2.getN() - 1).negate().add(e);
        ct2.add(pk.getG().duplicate().mul(mu));


        // Dec
//        Element muPrime = s.mul(ct2.getColumnAt(ct2.getM() - 2));


        // Add
        Element ctSum = ct1.duplicate().add(ct2);

        // Multiply
        Element ctMul = ct1.duplicate().mul(
                new MP12PLP2MatrixSampler().init(pk).processElements(ct2)
        );
    }

}
