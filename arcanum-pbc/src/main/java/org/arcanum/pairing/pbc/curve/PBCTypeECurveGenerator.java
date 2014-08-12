package org.arcanum.pairing.pbc.curve;

import org.arcanum.common.parameters.ParametersGenerator;
import org.arcanum.common.parameters.PropertiesParameters;
import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCTypeECurveGenerator extends PBCCurveGenerator {
    protected int rbits, qbits;


    public PBCTypeECurveGenerator(int rbits, int qbits) {
        this.rbits = rbits;
        this.qbits = qbits;
    }


    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_e(fileName, rbits, qbits);
    }


    public static void main(String[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException("Too few arguments. Usage <rbits> <qbits>");

        if (args.length > 2)
            throw new IllegalArgumentException("Too many arguments. Usage <rbits> <qbits>");

        Integer rBits = Integer.parseInt(args[0]);
        Integer qBits = Integer.parseInt(args[1]);

        ParametersGenerator generator = new PBCTypeECurveGenerator(rBits, qBits);
        PropertiesParameters curveParams = (PropertiesParameters) generator.generate();

        System.out.println(curveParams.toString(" "));
    }

}