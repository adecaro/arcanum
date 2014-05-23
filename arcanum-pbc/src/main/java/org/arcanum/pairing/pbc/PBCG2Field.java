package org.arcanum.pairing.pbc;

import org.arcanum.Element;
import org.arcanum.pairing.pbc.wrapper.jna.PBCElementType;
import org.arcanum.pairing.pbc.wrapper.jna.PBCPairingType;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCG2Field extends PBCField {


    public PBCG2Field(PBCPairingType pairing) {
        super(pairing);
    }


    public Element newElement() {
        return new PBCCurvePointElement(
                new PBCElementType(PBCElementType.FieldType.G2, pairing),
                this
        );
    }

}