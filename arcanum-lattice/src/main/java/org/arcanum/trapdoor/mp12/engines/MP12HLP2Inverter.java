package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Vector;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.trapdoor.mp12.params.MP12HLP2InverterParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2Inverter extends AbstractElementCipher<Element, Vector, MP12HLP2InverterParameters> {

    protected MP12HLP2InverterParameters parameters;
    protected MP12PLP2Inverter inverter;


    public MP12HLP2Inverter() {
        this.inverter = new MP12PLP2Inverter();
    }


    public MP12HLP2Inverter init(MP12HLP2InverterParameters param) {
        this.parameters = param;
        this.inverter.init(parameters.getPk().getPrimitiveLatticPk());

        return this;
    }

    public Vector processElements(Element... input) {
        Vector b = (Vector) input[0];
        Vector hatB = (Vector) parameters.getSk().getR().mul(b.subVectorTo(inverter.n * 2));
        for (int i = 0, l = inverter.n * inverter.k; i < l; i++)
            hatB.getAt(i).add(b.getAt(2 * inverter.n + i));

        return inverter.processElements(hatB);
    }
}
