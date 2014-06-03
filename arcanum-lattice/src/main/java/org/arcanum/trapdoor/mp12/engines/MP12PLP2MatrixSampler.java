package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.util.concurrent.ExecutorServiceUtils;
import org.arcanum.util.concurrent.PoolExecutor;

import java.util.concurrent.ExecutorService;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2MatrixSampler extends MP12PLP2Sampler {

    protected static final ExecutorService EXECUTOR_SERVICE = ExecutorServiceUtils.getNewFixedThreadPool();


    protected MatrixField outputField;

    public MP12PLP2MatrixSampler(MatrixField outputField) {
        this.outputField = outputField;
    }

    public MP12PLP2MatrixSampler() {
    }

    @Override
    public Element processElements(Element... input) {
        final Matrix U = (Matrix) input[0];

        final Matrix result;
        if (outputField != null) {
            result = outputField.newElement();
        } else {
            result = new MatrixField<Field>(
                    parameters.getParameters().getRandom(),
                    parameters.getZq(),
                    parameters.getPreimageField().getN(), U.getM()
            ).newElement();
        }

        PoolExecutor pool = new PoolExecutor(EXECUTOR_SERVICE);
        for (int i = 0, length = U.getM(); i < length; i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
                    result.setColAt(finalI, MP12PLP2MatrixSampler.super.processElements(U.getColumnAt(finalI)));
                }
            });
        }
        pool.awaitTermination();

        return result;
    }
}
