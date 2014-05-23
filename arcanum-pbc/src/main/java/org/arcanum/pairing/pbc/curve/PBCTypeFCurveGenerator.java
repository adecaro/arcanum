package org.arcanum.pairing.pbc.curve;

import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCTypeFCurveGenerator extends PBCCurveGenerator {
    protected int rbits;


    public PBCTypeFCurveGenerator(int rbits) {
        this.rbits = rbits;
    }


    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_f(fileName, rbits);
    }
}