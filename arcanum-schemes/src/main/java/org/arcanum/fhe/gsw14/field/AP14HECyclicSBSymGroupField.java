package org.arcanum.fhe.gsw14.field;

import org.arcanum.field.base.AbstractVectorField;
import org.arcanum.permutation.Permutation;
import org.arcanum.util.concurrent.ExecutorServiceUtils;
import org.arcanum.util.concurrent.PoolExecutor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14HECyclicSBSymGroupField extends AbstractVectorField<GSW14Field, AP14HECyclicSBSymGroupElement> {


    public AP14HECyclicSBSymGroupField(GSW14Field field, int r) {
        super(field.getRandom(), field, r);
    }


    public AP14HECyclicSBSymGroupElement newElement() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public BigInteger getOrder() {
        return null;
    }

    public AP14HECyclicSBSymGroupElement getNqr() {
        return null;
    }

    public int getLengthInBytes() {
        return 0;
    }

    public AP14HECyclicSBSymGroupElement newElement(Object value) {
        if (value instanceof Permutation) {
            Permutation perm = (Permutation) value;

            if (perm.getSize() != n || !perm.isCyclic())
                throw new IllegalArgumentException("Permutation size not valid!!!");

            PoolExecutor executor = new PoolExecutor(ExecutorServiceUtils.getNewForAvailableProcessors());
            final GSW14Element[] elements = new GSW14Element[n];

            final int s = perm.permute(0);
            for (int i = 0; i < n; i++) {
                executor.submit(new ExecutorServiceUtils.IndexRunnable(i) {
                    public void run() {
                        elements[i] = targetField.newElement(i == s ? 1 : 0);
                    }
                });

            }
            executor.awaitTermination();

            return new AP14HECyclicSBSymGroupElement(this, elements);
        }

        throw new IllegalArgumentException("Input type not valid!!!");
    }

}
