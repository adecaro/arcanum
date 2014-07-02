package org.arcanum.fhe.gsw14.field;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.Vector;
import org.arcanum.field.base.AbstractField;
import org.arcanum.field.base.AbstractVectorField;
import org.arcanum.field.vector.VectorExElement;
import org.arcanum.trapdoor.mp12.generators.MP12PLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.MP12PLP2KeyPairGenerationParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GSW14Field extends AbstractField<GSW14Element> {

    private MP12PLP2PublicKeyParameters pk;
    private Element s, sDec;
    private BigInteger oneFourthOrder;


    public GSW14Field(SecureRandom random, int n, int k) {
        super(random);

        // Init Micciancio-Peikert Primitive Lattice PK
        MP12PLP2KeyPairGenerator gen = new MP12PLP2KeyPairGenerator();
        gen.init(new MP12PLP2KeyPairGenerationParameters(random, n, k));

        this.pk = (MP12PLP2PublicKeyParameters) gen.generateKeyPair().getPublic();

        // Generate Secret Key
        this.s = pk.getPreimageField()
                .newField(n - 1)
                .newElementFromSampler(MP12P2Utils.getLWENoiseSampler(random, n));

        this.sDec = new VectorExElement(
                (AbstractVectorField) s.getField(),
                (Vector) s,
                pk.getZq().newOneElement()
        );

        this.oneFourthOrder = pk.getG().getAt(0, k-2).toBigInteger().shiftRight(2);
    }


    public GSW14Element newElement() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    @Override
    public GSW14Element newElement(int value) {
        return new GSW14Element(this, encrypt(value));
    }

    public BigInteger getOrder() {
        return null;
    }

    public GSW14Element getNqr() {
        return null;
    }

    public int getLengthInBytes() {
        return 0;
    }


    public MP12PLP2PublicKeyParameters getPk() {
        return pk;
    }


    private Matrix encrypt(int value) {
        // value must be in {0,1}
        Element mu = pk.getZq().newElement(value);

        // Sample error
        Element e = pk.getPreimageField().newElementFromSampler(MP12P2Utils.getLWENoiseSampler(random, pk.getParameters().getN()));
        System.out.println("After e");

        // Generate cts
        Matrix ct = (Matrix) pk.getG().getField().newElement();
        System.out.println("ct instantiated ");

        ct.setRowsToRandom(0, ct.getN() - 1)
                .getRowsViewAt(0, ct.getN() - 1)
                .mulTo(s, ct.getRowViewAt(ct.getN() - 1));
        ct.getRowViewAt(ct.getN() - 1).negate().add(e);
        ct.add(pk.getG().duplicate().mul(mu));

        return ct;
    }


    public BigInteger decrypt(Matrix value) {
        if (s == null)
            throw new IllegalStateException("Cannot decrypt ciphertext. Secret not initialized");

        BigInteger result = sDec.mul(value.getViewColAt(value.getM() - 2)).toBigInteger();
        System.out.println("result = " + result);
        return result.abs().compareTo(oneFourthOrder) >= 0 ? BigInteger.ONE : BigInteger.ZERO;
    }
}
