package org.arcanum.fhe.gsw14.field;

import org.arcanum.field.vector.AbstractMatrixField;
import org.arcanum.permutation.Permutation;
import org.arcanum.util.concurrent.ExecutorServiceUtils;
import org.arcanum.util.concurrent.PoolExecutor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14HESymGroupField extends AbstractMatrixField<AP14GSW14Field, AP14HESymGroupElement> {

    private int rSquare;


    public AP14HESymGroupField(AP14GSW14Field field, int r) {
        super(field.getRandom(), field, r, r);

        this.rSquare = r * r;
    }


    public AP14HESymGroupElement newElement() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public BigInteger getOrder() {
        return null;
    }

    public AP14HESymGroupElement getNqr() {
        return null;
    }

    public int getLengthInBytes() {
        return 0;
    }

    public AP14HESymGroupElement newElement(Object value) {
        if (value instanceof Permutation) {
            Permutation perm = (Permutation) value;

            if (perm.getSize() != n)
                throw new IllegalArgumentException("Permutation size not valid!!!");

            PoolExecutor executor = new PoolExecutor(ExecutorServiceUtils.getNewForAvailableProcessors());
            final AP14GSW14Element[][] elements = new AP14GSW14Element[n][m];
            for (int i = 0; i < n; i++) {
                final int s = perm.permute(i);

                for (int j = 0; j < n; j++) {
                    executor.submit(new ExecutorServiceUtils.IndexRunnable(i, j, s) {
                        public void run() {
                            elements[i][j] = targetField.newElement(j == s ? 1 : 0);
                        }
                    });
                }

            }
            executor.awaitTermination();

            return new AP14HESymGroupElement(this, elements);
        }

        throw new IllegalArgumentException("Input type not valid!!!");
    }

}
