package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.common.concurrent.ExecutorServiceUtils;
import org.arcanum.common.concurrent.PoolExecutor;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleParameters;

import java.util.concurrent.ExecutorService;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2MatrixSampler extends AbstractElementCipher<Element, Matrix, MP12HLP2SampleParameters> {

    protected static final ExecutorService EXECUTOR_SERVICE = ExecutorServiceUtils.getNewFixedThreadPool();


    protected MatrixField outputField;
    protected MP12HLP2Sampler sampler;

    public MP12HLP2MatrixSampler(MatrixField outputField) {
        this.outputField = outputField;
        this.sampler = new MP12HLP2Sampler();
    }


    @Override
    public MP12HLP2MatrixSampler init(MP12HLP2SampleParameters param) {
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
                        sampler.pk.getParameters().getRandom(),
                        sampler.pk.getPrimitiveLatticPk().getZq(),
                        sampler.pk.getM(), U.getM()
            ).newElement();
        }

        PoolExecutor pool = new PoolExecutor(EXECUTOR_SERVICE);
        for (int i = 0, length = U.getM(); i < length; i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
                    result.setColAt(finalI, sampler.processElements(U.getColumnAt(finalI)));
                }
            });
        }
        pool.awaitTermination();

//        for (int i = 0, length = result.getN(); i < length; i++) {
//            result.setColAt(i, super.processElements(U.getColumnAt(i)));
//        }


        return result;
    }
}
