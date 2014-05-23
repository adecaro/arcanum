package org.arcanum.pairing.pbc;

import org.arcanum.Element;
import org.arcanum.pairing.pbc.wrapper.jna.PBCElementType;
import org.arcanum.pairing.pbc.wrapper.jna.PBCPairingType;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCGTField extends PBCField {


    public PBCGTField(PBCPairingType pairing) {
        super(pairing);
    }


    public Element newElement() {
        return new PBCElement(
                new PBCElementType(PBCElementType.FieldType.GT, pairing),
                this
        );
    }

}