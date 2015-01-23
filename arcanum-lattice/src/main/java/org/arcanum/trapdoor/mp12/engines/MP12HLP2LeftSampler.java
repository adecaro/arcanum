package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Vector;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleLeftParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2LeftSampler extends AbstractElementCipher<Element, Vector, MP12HLP2SampleLeftParameters> {


    protected MatrixField<Field> extendedAField;
    protected MP12HLP2Sampler sampler;

    public MP12HLP2LeftSampler() {
        this.sampler = new MP12HLP2Sampler();
    }

    public MP12HLP2LeftSampler init(MP12HLP2SampleLeftParameters param) {
        // Set Extension Length
        sampler.setMatrixExtensionLength(param.getMatrixExtensionLength());

        // Setup sampler
        sampler.init(param);

        // Setup extendedAField
        this.extendedAField = new MatrixField<Field>(
                sampler.pk.getParameters().getRandom(),
                sampler.pk.getPrimitiveLatticPk().getZq(),
                sampler.pk.getParameters().getN(),
                sampler.pk.getM() + sampler.matrixExtensionLength
        );

        return this;
    }

    @Override
    public Vector processElements(Element... input) {
        Matrix M = (Matrix) input[0];
        Element u = input[1].duplicate();

        // Extend matrix A with M
        sampler.A = extendedAField.newTwoByColMatrix(sampler.pk.getA(), M);

        // Process u
        return sampler.processElements(u);
    }
}
