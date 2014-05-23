package org.arcanum.fe.abe.gghsw13.params;

import org.arcanum.Element;
import org.arcanum.circuit.BooleanCircuit;

import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13SecretKeyParameters extends GGHSW13KeyParameters {

    private BooleanCircuit circuit;
    private Map<Integer, Element[]> keys;


    public GGHSW13SecretKeyParameters(GGHSW13Parameters parameters, BooleanCircuit circuit, Map<Integer, Element[]> keys) {
        super(true, parameters);

        this.circuit = circuit;
        this.keys = keys;
    }

    public BooleanCircuit getCircuit() {
        return circuit;
    }

    public Element[] getKeyElementsAt(int index) {
        return keys.get(index);
    }
}