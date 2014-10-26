package org.arcanum.fhe.ap14.field;

import org.arcanum.*;
import org.arcanum.Matrix.ColumnReader;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.field.base.AbstractField;
import org.arcanum.field.base.AbstractVectorField;
import org.arcanum.field.vector.*;
import org.arcanum.field.z.ZFieldSelector;
import org.arcanum.trapdoor.mp12.engines.MP12PLP2LongSampler;
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
    protected Vector s, decryptionKey;
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

        this.decryptionKey = new VectorExElement<Element>(
                (AbstractVectorField) s.getField(),
                s,
                pk.getZq().newOneElement()
        );

        this.oneFourthOrder = pk.getG().getAt(0, k - 2).toBigInteger().shiftRight(2);

        this.sampler = new MP12PLP2LongSampler().init(pk);
//        this.sampler = new MP12PLP2Sampler().init(pk);
    }

    public AP14GSW14Field(SecureRandom random, int n, int k, Element externalKey) {
        super(random);

        // Init Micciancio-Peikert Primitive Lattice PK
        MP12PLP2KeyPairGenerator gen = new MP12PLP2KeyPairGenerator();
        gen.init(new MP12PLKeyPairGenerationParameters(random, n, k));

        this.pk = (MP12PLPublicKeyParameters) gen.generateKeyPair().getPublic();
        this.lweErrorSampler = MP12P2Utils.getLWENoiseSampler(random, n);

        // Import Secret Key
        this.s = pk.getPreimageField().newField(n - 1).newElement(externalKey);

        this.decryptionKey = new VectorExElement<Element>(
                (AbstractVectorField) this.s.getField(),
                (Vector<Element>) this.s,
                pk.getZq().newOneElement()
        );

        this.oneFourthOrder = pk.getG().getAt(0, k - 2).toBigInteger().shiftRight(2);

        this.sampler = new MP12PLP2LongSampler().init(pk);
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

    public Element newEmptyElement() {
        return newElementErrorFreeFullMatrix(0);
    }


    public MP12PLPublicKeyParameters getPk() {
        return pk;
    }


    public Vector getS() {
        return s;
    }

    public Vector getS(BigInteger order) {
        return new VectorField<Field>(
                random,
                ZFieldSelector.getInstance().getSymmetricZrField(random, order),
                s.getSize()
        ).newElement(s);
    }

    public Vector getDecryptionKey() {
        return decryptionKey;
    }

    public Vector getDecryptionKey(BigInteger order) {
        return new VectorField<Field>(
                random,
                ZFieldSelector.getInstance().getSymmetricZrField(random, order),
                decryptionKey.getSize()
        ).newElement(decryptionKey);
    }


    protected Matrix encrypt(int value) {
        // value must be in {0,1}
        Element mu = pk.getZq().newElement(value);

        // Generate cts
        Matrix ct = (Matrix) pk.getG().getField().newElement();

        ct.setRowsToRandom(0, ct.getN() - 1)
                .getViewRowsAt(0, ct.getN() - 1)
                .mulTo(s, ct.getViewRowAt(ct.getN() - 1));
        ct.getViewRowAt(ct.getN() - 1).negate().add(lweErrorSampler);
        ct.add(pk.getG().duplicate().mul(mu));

        return ct;
    }

    protected BigInteger decrypt(Matrix value) {
        if (s == null)
            throw new IllegalStateException("Cannot decrypt ciphertext. Secret not initialized");

        BigInteger result = decryptionKey.mul(value.getViewColAt(value.getM() - 2)).toBigInteger();
        System.out.println("res = " + result.abs());
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

    public BigInteger decrypt(Element c2) {
        BigInteger result = decryptionKey.mul(c2).toBigInteger();
        System.out.println("res = " + result.abs());
//        System.out.println("ofo = " + oneFourthOrder);
//        System.out.println("q   = " + pk.getZq().getOrder());
        return result.abs().compareTo(oneFourthOrder) >= 0 ? BigInteger.ONE : BigInteger.ZERO;
    }

    public BigInteger decrypt(Element c2, Element decryptionKey) {
        BigInteger result = decryptionKey.mul(c2).toBigInteger();
        System.out.println("res = " + result.abs());
//        System.out.println("ofo = " + oneFourthOrder);
//        System.out.println("q   = " + pk.getZq().getOrder());
        return result.abs().compareTo(oneFourthOrder) >= 0 ? BigInteger.ONE : BigInteger.ZERO;
    }

}
