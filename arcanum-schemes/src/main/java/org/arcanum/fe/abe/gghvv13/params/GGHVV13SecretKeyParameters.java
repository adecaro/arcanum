package org.arcanum.fe.abe.gghvv13.params;

import org.arcanum.Element;
import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.field.util.ElementUtils;

import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHVV13SecretKeyParameters extends GGHVV13KeyParameters {

    private BooleanCircuit circuit;
    private Element[][] M;
    private Map<Integer, Element[]> keys;


    public GGHVV13SecretKeyParameters(GGHVV13Parameters parameters,
                                      BooleanCircuit circuit,
                                      Map<Integer, Element[]> keys,
                                      Element[][] M) {
        super(true, parameters);

        this.circuit = circuit;
        this.keys = ElementUtils.cloneImmutable(keys);
        this.M = M;
    }

    public BooleanCircuit getCircuit() {
        return circuit;
    }

    public Element[] getKeyElementsAt(int index) {
        return keys.get(index);
    }

    public Element getMAt(int i, int j) {
        return M[i][j];
    }

}