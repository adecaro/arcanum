package org.arcanum.tor.gvw13.generators;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.common.cipher.generators.ElementKeyGenerator;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.cipher.params.ElementKeyGenerationParameters;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.TwoByRowMatrixElement;
import org.arcanum.tor.gvw13.params.TORGVW13ReKeyGenerationParameters;
import org.arcanum.tor.gvw13.params.TORGVW13RecodeParameters;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2MatrixSampler;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleParameters;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TORGVW13RecKeyGenerator implements ElementKeyGenerator {
    private TORGVW13ReKeyGenerationParameters params;


    public ElementKeyGenerator init(ElementKeyGenerationParameters keyGenerationParameters) {
        this.params = (TORGVW13ReKeyGenerationParameters) keyGenerationParameters;

        return this;
    }

    public ElementCipherParameters generateKey() {
        MP12HLP2PublicKeyParameters latticePk = (MP12HLP2PublicKeyParameters) params.getLeftPk().getPublicKeyParameters();

        MatrixField<Field> RField = new MatrixField<Field>(
                latticePk.getParameters().getRandom(),
                latticePk.getPrimitiveLatticPk().getZq(),
                latticePk.getM()
        );

        Matrix R0 = null, R1 = null;
        if (params.isLeft()) {
            // b = 0

            // Sample R1 from D_Z,s
            // TODO: The s here is that of SampleD
            R1 = RField.newElementFromSampler(
                    MP12P2Utils.getLWENoiseSampler(latticePk.getParameters().getRandom(), latticePk.getParameters().getN())
                    );

            // Compute U
            // TODO: compute differently as -A_r R_1 + A_T
            Matrix U = (Matrix) ((MP12HLP2PublicKeyParameters) params.getTargetPk().getPublicKeyParameters()).getA().duplicate().sub(
                    ((MP12HLP2PublicKeyParameters) params.getRightPk().getPublicKeyParameters()).getA().mul(R1)
            );

            // Sample R0
            ElementCipher sampleD = new MP12HLP2MatrixSampler(RField);
            sampleD.init(new MP12HLP2SampleParameters(params.getLeftPk().getPublicKeyParameters(), params.getSk().getPrivateKeyParameter()));
            R0 = (Matrix) sampleD.processElements(U);
        } else {
            // b = 1
            // Sample R0 from D_Z,s
            // TODO: The s here is that of SampleD
            R0 = RField.newElementFromSampler(
                    MP12P2Utils.getLWENoiseSampler(latticePk.getParameters().getRandom(), latticePk.getParameters().getN())
            );

            // Compute U
            Matrix U = (Matrix) ((MP12HLP2PublicKeyParameters) params.getTargetPk().getPublicKeyParameters()).getA().duplicate().sub(
                    ((MP12HLP2PublicKeyParameters) params.getLeftPk().getPublicKeyParameters()).getA().mul(R0)
            );

            // Sample R1
            ElementCipher sampleD = new MP12HLP2MatrixSampler(RField);
            sampleD.init(new MP12HLP2SampleParameters(params.getLeftPk().getPublicKeyParameters(), params.getSk().getPrivateKeyParameter()));
            R1 = (Matrix) sampleD.processElements(U);
        }

        // Compute R
        Element R = new TwoByRowMatrixElement(R0, R1);

        return new TORGVW13RecodeParameters(params.getLeftPk().getParameters(), R);
    }


}