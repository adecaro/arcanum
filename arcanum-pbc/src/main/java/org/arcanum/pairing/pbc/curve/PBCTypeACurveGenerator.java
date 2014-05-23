package org.arcanum.pairing.pbc.curve;

import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCTypeACurveGenerator extends PBCCurveGenerator {
    protected int rbits, qbits;


    public PBCTypeACurveGenerator(int rbits, int qbits) {
        this.rbits = rbits;
        this.qbits = qbits;
    }


    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_a(fileName, rbits, qbits);
    }
}
