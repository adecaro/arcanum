package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Vector;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.trapdoor.mp12.params.MP12HLP2OneWayFunctionParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2OneWayFunction extends AbstractElementCipher<Element, Vector, MP12HLP2OneWayFunctionParameters> {

    protected MP12HLP2OneWayFunctionParameters parameters;

    public MP12HLP2OneWayFunction init(MP12HLP2OneWayFunctionParameters param) {
        this.parameters = param;

        return this;
    }

    public Vector processElements(Element... input) {
        Vector vector = (Vector) parameters.getPk().getA().mul(input[0]);

        Vector error = (Vector) vector.getField().newElement();
        for (int i = 0; i < vector.getSize(); i++)
            error.getAt(i).set(parameters.getSampler().sample());

        return (Vector) vector.add(error);
    }

}
