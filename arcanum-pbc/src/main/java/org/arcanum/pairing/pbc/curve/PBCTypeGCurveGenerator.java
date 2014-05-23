package org.arcanum.pairing.pbc.curve;

import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCTypeGCurveGenerator extends PBCCurveGenerator {
    protected int discriminant;


    public PBCTypeGCurveGenerator(int discriminant) {
        this.discriminant = discriminant;
    }


    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_g(fileName, discriminant);
    }
}