package org.arcanum.pairing.mm.clt13.pairing;

import org.arcanum.*;
import org.arcanum.pairing.accumulator.PairingAccumulator;
import org.arcanum.pairing.accumulator.PairingAccumulatorFactory;
import org.arcanum.pairing.map.DefaultPairingPreProcessing;
import org.arcanum.pairing.mm.clt13.engine.CTL13MMInstance;
import org.arcanum.pairing.mm.clt13.engine.MultiThreadCTL13MMInstance;
import org.arcanum.pairing.mm.clt13.generators.CTL13MMPublicParameterGenerator;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 2.0.0
 */
public class CTL13MMPairing implements Pairing {

    protected SecureRandom random;
    protected CTL13MMInstance instance;
    protected CTL13MMField[] fields;


    public CTL13MMPairing(SecureRandom random, CTL13MMInstance instance) {
        this.random = random;
        this.instance = instance;
        this.fields = new CTL13MMField[instance.getSystemParameters().getKappa() + 1];
    }


    public CTL13MMPairing(SecureRandom random, Parameters parameters) {
        this.random = random;
        this.instance = initInstance(parameters);
        this.fields = new CTL13MMField[instance.getSystemParameters().getKappa() + 1];
    }


    public boolean isSymmetric() {
        return true;
    }

    public Field getG1() {
        return getFieldAt(1);
    }

    public Field getG2() {
        return getFieldAt(1);
    }

    public Field getGT() {
        return getFieldAt(2);
    }

    public Field getZr() {
        return getFieldAt(0);
    }

    public int getDegree() {
        return instance.getSystemParameters().getKappa();
    }

    public Field getFieldAt(int index) {
        if (fields[index] == null)
            fields[index] = new CTL13MMField(this, index);

        return fields[index];
    }

    public Element pairing(Element in1, Element in2) {
        CTL13MMElement a = (CTL13MMElement) in1;
        CTL13MMElement b = (CTL13MMElement) in2;

        int index = a.index + b.index;
        BigInteger value = instance.reduce(a.value.multiply(b.value));

        return new CTL13MMElement((CTL13MMField) getFieldAt(index), value, index);
    }

    public boolean isProductPairingSupported() {
        return false;
    }

    public Element pairing(Element[] in1, Element[] in2) {
        if (in1.length != in2.length)
            throw new IllegalArgumentException("Array lengths mismatch.");

        PairingAccumulator combiner = PairingAccumulatorFactory.getInstance().getPairingMultiplier(this);
        for (int i = 0; i < in1.length; i++)
            combiner.addPairing(in1[i], in2[i]);
        return combiner.awaitResult();
    }

    public PairingPreProcessing getPairingPreProcessingFromElement(Element in1) {
        return new DefaultPairingPreProcessing(this, in1);
    }

    public int getPairingPreProcessingLengthInBytes() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PairingPreProcessing getPairingPreProcessingFromBytes(byte[] source) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PairingPreProcessing getPairingPreProcessingFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getFieldIndex(Field field) {
        for (int i = 0; i < fields.length; i++)
            if (fields[i] == field)
                return i;

        return -1;
    }

    public CTL13MMInstance getCTL13MMInstance() {
        return instance;
    }


    protected CTL13MMInstance initInstance(Parameters parameters) {
        return new MultiThreadCTL13MMInstance(random,
                new CTL13MMPublicParameterGenerator(
                        random,
                        parameters
                ).generate()
        );
    }

}
