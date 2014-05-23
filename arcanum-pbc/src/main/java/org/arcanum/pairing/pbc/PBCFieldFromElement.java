package org.arcanum.pairing.pbc;

import org.arcanum.Element;
import org.arcanum.pairing.pbc.wrapper.jna.PBCElementType;
import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCFieldFromElement extends PBCField {

    public PBCFieldFromElement(PBCElementType baseElement) {
        super(baseElement);
    }

    public Element newElement() {
        PBCElementType element = new PBCElementType();
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_init_same_as(element, baseElement);

        return new PBCElement(element, this);
    }
}
