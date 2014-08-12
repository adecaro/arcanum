package org.arcanum.fe.abe.gvw13.params;

import org.arcanum.ElementCipherParameters;
import org.arcanum.Field;
import org.arcanum.common.fe.params.KeyParameters;
import org.arcanum.program.circuit.BooleanCircuit;

import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13SecretKeyParameters extends KeyParameters<GVW13Parameters> {

    private BooleanCircuit circuit;
    private Map<Integer, ElementCipherParameters[]> keys;
    private Field ciphertextElementField;
    private ElementCipherParameters cipherParametersOut;

    public GVW13SecretKeyParameters(GVW13Parameters parameters,
                                    BooleanCircuit circuit,
                                    Map<Integer, ElementCipherParameters[]> keys,
                                    Field ciphertextElementField,
                                    ElementCipherParameters cipherParametersOut) {
        super(true, parameters);

        this.circuit = circuit;
        this.keys = keys;
        this.ciphertextElementField = ciphertextElementField;
        this.cipherParametersOut = cipherParametersOut;
    }

    public BooleanCircuit getCircuit() {
        return circuit;
    }

    public ElementCipherParameters getCipherParametersAt(int index, int b0, int b1) {
        if (b0 == 0 && b1 == 0)
            return keys.get(index)[0];
        if (b0 == 0 && b1 == 1)
            return keys.get(index)[1];
        if (b0 == 1 && b1 == 0)
            return keys.get(index)[2];
        if (b0 == 1 && b1 == 1)
            return keys.get(index)[3];

        throw new IllegalStateException("Impossible!!!");
    }

    public Field getCiphertextElementField() {
        return ciphertextElementField;
    }

    public ElementCipherParameters getCipherParametersOut() {
        return cipherParametersOut;
    }
}