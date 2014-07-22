package org.arcanum.fhe.gsw14.field;

import org.arcanum.*;
import org.arcanum.Matrix.ColumnReader;
import org.arcanum.field.base.AbstractField;
import org.arcanum.field.base.AbstractVectorField;
import org.arcanum.field.vector.AbstractMatrixField;
import org.arcanum.field.vector.ArrayMatrixElement;
import org.arcanum.field.vector.VectorExElement;
import org.arcanum.field.vector.ZeroMatrixElement;
import org.arcanum.trapdoor.mp12.engines.MP12PLP2MatrixSampler;
import org.arcanum.trapdoor.mp12.engines.MP12PLP2Sampler;
import org.arcanum.trapdoor.mp12.generators.MP12PLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.MP12PLKeyPairGenerationParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLPublicKeyParameters;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14GSW14Field extends AbstractField<AP14GSW14Element> {

    protected MP12PLPublicKeyParameters pk;
    protected Vector s, sDec;
    protected BigInteger oneFourthOrder;
    protected ElementCipher sampler;
    protected Sampler<BigInteger> lweErrorSampler;


    public AP14GSW14Field(SecureRandom random, int n, int k) {
        super(random);

        // Init Micciancio-Peikert Primitive Lattice PK
        MP12PLP2KeyPairGenerator gen = new MP12PLP2KeyPairGenerator();
        gen.init(new MP12PLKeyPairGenerationParameters(random, n, k));

        this.pk = (MP12PLPublicKeyParameters) gen.generateKeyPair().getPublic();
        this.lweErrorSampler = MP12P2Utils.getLWENoiseSampler(random, n);

        // Generate Secret Key
        this.s = pk.getPreimageField()
                .newField(n - 1)
                .newElementFromSampler(lweErrorSampler);

        System.out.println("s = " + s);

        this.sDec = new VectorExElement<Element>(
                (AbstractVectorField) s.getField(),
                s,
                pk.getZq().newOneElement()
        );

        this.oneFourthOrder = pk.getG().getAt(0, k - 2).toBigInteger().shiftRight(2);

        this.sampler = new MP12PLP2Sampler().init(pk);
    }

    public AP14GSW14Field(SecureRandom random, int n, int k, Element s) {
        super(random);

        // Init Micciancio-Peikert Primitive Lattice PK
        MP12PLP2KeyPairGenerator gen = new MP12PLP2KeyPairGenerator();
        gen.init(new MP12PLKeyPairGenerationParameters(random, n, k));

        this.pk = (MP12PLPublicKeyParameters) gen.generateKeyPair().getPublic();

        // Generate Secret Key
        this.s = pk.getPreimageField().newField(n - 1).newElement(s);

        this.sDec = new VectorExElement<Element>(
                (AbstractVectorField) s.getField(),
                (Vector<Element>) s,
                pk.getZq().newOneElement()
        );

        this.oneFourthOrder = pk.getG().getAt(0, k - 2).toBigInteger().shiftRight(2);

        this.sampler = new MP12PLP2MatrixSampler().init(pk);
    }



    public AP14GSW14Element newElement() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public BigInteger getOrder() {
        return null;
    }

    public AP14GSW14Element getNqr() {
        return null;
    }

    public int getLengthInBytes() {
        return 0;
    }

    public AP14GSW14Element newElement(int value) {
        return new AP14GSW14Element(this, encrypt(value));
    }

    public AP14GSW14Element newZeroElement() {
        return newElement(0);
    }

    public AP14GSW14Element newOneElement() {
        return newElement(1);
    }


    public Element newElementErrorFree(int value) {
        switch (value) {
            case 0 :
                return new AP14GSW14Element(this, new ZeroMatrixElement((AbstractMatrixField) pk.getG().getField()));
            case 1 :
                return new AP14GSW14Element(this, (Matrix) pk.getG().duplicate());
        }
        throw new IllegalArgumentException("Value not valid. Must be 0 or 1.");
    }

    public Element newElementErrorFreeFullMatrix(int value) {
        switch (value) {
            case 0 :
                return new AP14GSW14Element(
                        this,
                        new ArrayMatrixElement((AbstractMatrixField) pk.getG().getField())
                );
            case 1 :
                return new AP14GSW14Element(
                        this,
                        new ArrayMatrixElement((AbstractMatrixField) pk.getG().getField(), pk.getPrimitiveVector())
                );
        }
        throw new IllegalArgumentException("Value not valid. Must be 0 or 1.");
    }


    public MP12PLPublicKeyParameters getPk() {
        return pk;
    }


    protected Matrix encrypt(int value) {
        // value must be in {0,1}
        Element mu = pk.getZq().newElement(value);

        // Sample error
        Element e = pk.getPreimageField().newElementFromSampler(lweErrorSampler);

        // Generate cts
        Matrix ct = (Matrix) pk.getG().getField().newElement();

        ct.setRowsToRandom(0, ct.getN() - 1)
                .getViewRowsAt(0, ct.getN() - 1)
                .mulTo(s, ct.getViewRowAt(ct.getN() - 1));
        ct.getViewRowAt(ct.getN() - 1).negate().add(e);
        ct.add(pk.getG().duplicate().mul(mu));

        return ct;
    }

    protected BigInteger decrypt(Matrix value) {
        if (s == null)
            throw new IllegalStateException("Cannot decrypt ciphertext. Secret not initialized");

        BigInteger result = sDec.mul(value.getViewColAt(value.getM() - 2)).toBigInteger();
//        System.out.println("res = " + result.abs());
//        System.out.println("ofo = " + oneFourthOrder);
//        System.out.println("q   = " + pk.getZq().getOrder());
        return result.abs().compareTo(oneFourthOrder) >= 0 ? BigInteger.ONE : BigInteger.ZERO;
    }

    protected ColumnReader gInvert(final Matrix value) {
        return new ColumnReader() {
            public int getM() {
                return value.getM();
            }

            public Vector getColumnAt(int column) {
                return (Vector) sampler.processElements(value.getViewColAt(column));
            }
        };
    }

    public Element newEmptyElement() {
        return newElementErrorFreeFullMatrix(0);
    }
}
