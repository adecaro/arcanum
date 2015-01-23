package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.common.concurrent.ExecutorServiceUtils;
import org.arcanum.common.concurrent.PoolExecutor;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;

import java.util.concurrent.ExecutorService;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2MatrixSampler extends AbstractElementCipher<Element, Matrix, MP12PLP2PublicKeyParameters> {

    protected static final ExecutorService EXECUTOR_SERVICE = ExecutorServiceUtils.getNewFixedThreadPool();


    protected MatrixField outputField;
    protected MP12PLP2Sampler sampler;


    public MP12PLP2MatrixSampler() {
        this.sampler = new MP12PLP2Sampler();
    }


    @Override
    public MP12PLP2MatrixSampler init(MP12PLP2PublicKeyParameters param) {
        this.sampler.init(param);

        return this;
    }

    @Override
    public Matrix processElements(Element... input) {
        final Matrix U = (Matrix) input[0];

        final Matrix result;
        if (outputField != null) {
            result = outputField.newElement();
        } else {
            result = new MatrixField<Field>(
                    this.sampler.parameters.getParameters().getRandom(),
                    this.sampler.parameters.getZq(),
                    this.sampler.parameters.getPreimageField().getN(), U.getM()
            ).newElement();
        }


        PoolExecutor pool = new PoolExecutor(EXECUTOR_SERVICE);
        for (int i = 0, length = U.getM(); i < length; i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
                    sampler.processElementsTo(
                            result.getViewColAt(finalI),
                            U.getViewColAt(finalI)
                    );
                }
            });
        }
        pool.awaitTermination();

        return result;
    }
}
