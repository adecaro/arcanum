package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.*;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleLeftParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2LeftSampler extends MP12HLP2Sampler {


    protected MatrixField<Field> extendedAField;


    public ElementCipher init(ElementCipherParameters param) {
        MP12HLP2SampleLeftParameters params = (MP12HLP2SampleLeftParameters) param;

        // Set Extension Length
        setMatrixExtensionLength(params.getMatrixExtensionLength());

        // Setup sampler
        super.init(params);

        // Setup extendedAField
        this.extendedAField = new MatrixField<Field>(
                pk.getParameters().getRandom(),
                pk.getZq(),
                pk.getParameters().getN(),
                pk.getM() + matrixExtensionLength
        );

        return this;
    }

    @Override
    public Element processElements(Element... input) {
        Matrix M = (Matrix) input[0];
        Element u = input[1].duplicate();

        // Extend matrix A with M
        this.A = extendedAField.newTwoByColMatrix(pk.getA(), M);

        // Process u
        return super.processElements(u);
    }
}
