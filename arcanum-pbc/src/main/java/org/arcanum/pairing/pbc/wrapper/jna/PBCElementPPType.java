package org.arcanum.pairing.pbc.wrapper.jna;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCElementPPType extends Memory {

    public PBCElementPPType() {
        super(WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_sizeof());
    }

    public PBCElementPPType(Pointer element) {
        this();
        
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_init(this, element);
    }

    @Override
    protected void finalize() {
        if (isValid()) {
            WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_clear(this);
            super.finalize();
        }
    }
}