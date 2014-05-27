package org.arcanum.tor.gvw13.generators;

import org.arcanum.Element;
import org.arcanum.ElementCipherParameters;
import org.arcanum.Field;
import org.arcanum.field.vector.MatrixElement;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.tor.gvw13.params.TORGVW13ReKeyGenerationParameters;
import org.arcanum.tor.gvw13.params.TORGVW13RecodeParameters;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2MatrixSampler;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2Sampler;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleParameters;
import org.arcanum.util.cipher.generators.ElementKeyGenerator;
import org.arcanum.util.cipher.params.ElementKeyGenerationParameters;

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
                latticePk.getZq(),
                latticePk.getM()
        );

        Element R0 = null, R1 = null;
        if (params.isLeft()) {
            // b = 0

            // Sample R1 from D_Z,s
            // TODO: The s here is that of SampleD
//            R1 = RField.newElementFromSampler(latticePk.getDiscreteGaussianSampler());

            // Compute U
            MatrixElement U = (MatrixElement) ((MP12HLP2PublicKeyParameters) params.getTargetPk().getPublicKeyParameters()).getA().duplicate().sub(
                    ((MP12HLP2PublicKeyParameters) params.getRightPk().getPublicKeyParameters()).getA().mul(R1)
            );

            // Sample R0
            MP12HLP2Sampler sampleD = new MP12HLP2MatrixSampler(RField);
            sampleD.init(new MP12HLP2SampleParameters(params.getLeftPk().getPublicKeyParameters(), params.getSk().getPrivateKeyParameter()));
            R0 = sampleD.processElements(U);
        } else {
            // b = 1
            // Sample R0 from D_Z,s
            // TODO: The s here is that of SampleD
//            R0 = RField.newElementFromSampler(latticePk.getDiscreteGaussianSampler());

            // Compute U
            MatrixElement U = (MatrixElement) ((MP12HLP2PublicKeyParameters) params.getTargetPk().getPublicKeyParameters()).getA().duplicate().sub(
                    ((MP12HLP2PublicKeyParameters) params.getLeftPk().getPublicKeyParameters()).getA().mul(R0)
            );

            // Sample R1
            MP12HLP2Sampler sampleD = new MP12HLP2MatrixSampler(RField);
            sampleD.init(new MP12HLP2SampleParameters(params.getLeftPk().getPublicKeyParameters(), params.getSk().getPrivateKeyParameter()));
            R1 = sampleD.processElements(U);
        }

        // Compute R
        Element R = RField.unionByRow(R0, R1);

        return new TORGVW13RecodeParameters(params.getLeftPk().getParameters(), R);
    }


}