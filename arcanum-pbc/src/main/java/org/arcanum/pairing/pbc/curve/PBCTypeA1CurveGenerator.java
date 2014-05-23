package org.arcanum.pairing.pbc.curve;

import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCTypeA1CurveGenerator extends PBCCurveGenerator {


    public PBCTypeA1CurveGenerator() {
    }


    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_a1(fileName);
    }
}