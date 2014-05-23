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
public class MP12HLP2MatrixSampler extends MP12HLP2Sampler {

    protected static final ExecutorService EXECUTOR_SERVICE = ExecutorServiceUtils.getNewFixedThreadPool();


    protected MatrixField outputField;

    public MP12HLP2MatrixSampler(MatrixField outputField) {
        this.outputField = outputField;
    }

    public MP12HLP2MatrixSampler() {
    }

    @Override
    public Element processElements(Element... input) {
        final Matrix U = (Matrix) input[0];

        final Matrix result;
        if (outputField != null) {
            result = outputField.newElement();
        } else {
            result = new MatrixField<Field>(
                        pk.getParameters().getRandom(),
                        pk.getZq(),
                        pk.getM(), U.getM()
            ).newElement();
        }

        PoolExecutor pool = new PoolExecutor(EXECUTOR_SERVICE);
        for (int i = 0, length = U.getM(); i < length; i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
                    result.setColAt(finalI, MP12HLP2MatrixSampler.super.processElements(U.columnAt(finalI)));
                }
            });
        }
        pool.awaitTermination();

//        for (int i = 0, length = result.getN(); i < length; i++) {
//            result.setColAt(i, super.processElements(U.columnAt(i)));
//        }


        return result;
    }
}
