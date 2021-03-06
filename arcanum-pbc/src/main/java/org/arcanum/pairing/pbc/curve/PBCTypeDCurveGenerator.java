package org.arcanum.pairing.pbc.curve;

import org.arcanum.common.parameters.ParametersGenerator;
import org.arcanum.common.parameters.PropertiesParameters;
import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCTypeDCurveGenerator extends PBCCurveGenerator {
    protected int discriminant;


    public PBCTypeDCurveGenerator(int discriminant) {
        this.discriminant = discriminant;
    }

    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_d(fileName, discriminant);
    }


    public static void main(String[] args) {
        if (args.length < 3)
            throw new IllegalArgumentException("Too few arguments. Usage <discriminant> <rbits> <qbits>");

        if (args.length > 3)
            throw new IllegalArgumentException("Too many arguments. Usage <discriminant> <rbits> <qbits>");

        int discriminant = Integer.parseInt(args[0]);
        int rBits = Integer.parseInt(args[1]);
        int qBits = Integer.parseInt(args[2]);

        PropertiesParameters curveParams = null;
        for (; discriminant < 100000; discriminant++) {
            System.out.println("discriminant = " + discriminant);
            try {
                ParametersGenerator generator = new PBCTypeDCurveGenerator(discriminant);
                curveParams = (PropertiesParameters) generator.generate();

                int currentRBits = curveParams.getBigInteger("r").bitLength();
                int currentqBits = curveParams.getBigInteger("q").bitLength();

                System.out.println("rBits = " + currentRBits);
                System.out.println("qBits = " + currentqBits);

                if (currentRBits > rBits && currentqBits > qBits)
                    break;
                else
                    Thread.sleep(100);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

        System.out.println(curveParams.toString(" "));
    }

}