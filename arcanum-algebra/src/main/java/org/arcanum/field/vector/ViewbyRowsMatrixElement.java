package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ViewbyRowsMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected final Matrix base;
    protected final int start, end;

    public ViewbyRowsMatrixElement(AbstractMatrixField field, Matrix base, int start, int end) {
        super(new MatrixField<Field>(field.getRandom(), field.getTargetField(), end - start, field.getM()));

        this.base = base;
        this.start = start;
        this.end = end;
    }

    public ViewbyRowsMatrixElement(AbstractMatrixField field, Matrix base, int row) {
        super(new MatrixField<Field>(field.getRandom(), field.getTargetField(), 1, field.getM()));

        this.base = base;
        this.start = row;
        this.end = row;
    }


    @Override
    public boolean isZeroAt(int row, int col) {
        return base.isZeroAt(start + row, col);
    }

    @Override
    public E getAt(int row, int col) {
        return (E) base.getAt(start + row, col);
    }

}
