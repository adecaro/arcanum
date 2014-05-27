package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.ElementCipher;
import org.arcanum.ElementCipherParameters;
import org.arcanum.Vector;
import org.arcanum.trapdoor.mp12.params.MP12HLP2OneWayFunctionParameters;
import org.arcanum.util.cipher.engine.AbstractElementCipher;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2OneWayFunction extends AbstractElementCipher {

    protected MP12HLP2OneWayFunctionParameters parameters;

    public ElementCipher init(ElementCipherParameters param) {
        this.parameters = (MP12HLP2OneWayFunctionParameters) param;

        return this;
    }

    public Element processElements(Element... input) {
        Vector vector = (Vector) parameters.getPk().getA().mul(input[0]);

        Vector error = (Vector) vector.getField().newElement();
        for (int i = 0; i < vector.getSize(); i++)
            error.getAt(i).set(parameters.getSampler().sample());

        return vector.add(error);
    }

}
