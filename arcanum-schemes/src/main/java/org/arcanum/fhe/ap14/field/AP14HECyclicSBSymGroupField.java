package org.arcanum.fhe.ap14.field;

import org.arcanum.common.concurrent.ExecutorServiceUtils;
import org.arcanum.common.concurrent.PoolExecutor;
import org.arcanum.field.base.AbstractVectorField;
import org.arcanum.program.pbp.permutation.Permutation;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14HECyclicSBSymGroupField extends AbstractVectorField<AP14GSW14Field, AP14HECyclicSBSymGroupElement> {


    public AP14HECyclicSBSymGroupField(AP14GSW14Field field, int r) {
        super(field.getRandom(), field, r);
    }

    public AP14HECyclicSBSymGroupElement newElement() {
        throw new IllegalArgumentException("Not implemented yet!!!");
    }

    @Override
    public AP14HECyclicSBSymGroupElement newElement(BigInteger value) {
        PoolExecutor executor = new PoolExecutor(ExecutorServiceUtils.getCachedThreadPool());
        final AP14GSW14Element[] elements = new AP14GSW14Element[n];

        final int s = value.add(BigInteger.ONE).mod(BigInteger.valueOf(n)).intValue();
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

    @Override
    public AP14HECyclicSBSymGroupElement newElement(int value) {
        return newElement(BigInteger.valueOf(value));
    }

    @Override
    public AP14HECyclicSBSymGroupElement newOneElement() {
        return newElement(1);
    }

    @Override
    public AP14HECyclicSBSymGroupElement newZeroElement() {
        return newElement(0);
    }

    public AP14HECyclicSBSymGroupElement newElement(Object value) {
        if (value instanceof Permutation) {
            Permutation perm = (Permutation) value;
            if (!perm.isCyclic())
                throw new IllegalArgumentException("Not a cyclic permutation!!!");

            PoolExecutor executor = new PoolExecutor(ExecutorServiceUtils.getCachedThreadPool());
            final AP14GSW14Element[] elements = new AP14GSW14Element[n];

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

        throw new IllegalArgumentException("Invalid argument!!!");
    }

}
