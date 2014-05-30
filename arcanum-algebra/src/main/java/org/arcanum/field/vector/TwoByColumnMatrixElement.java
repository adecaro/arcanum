package org.arcanum.field.vector;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TwoByColumnMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected AbstractMatrixElement A, B;
    protected int Am;


    public TwoByColumnMatrixElement(MatrixField field, AbstractMatrixElement A, AbstractMatrixElement B) {
        super(field);

        this.A = A;
        this.B = B;

        this.Am = A.getM();
    }

    
    public final E getAt(int row, int col) {
        if (col < Am)
            return (E) A.getAt(row, col);
        else
            return (E) B.getAt(row, col - Am);
    }

    public final boolean isZeroAt(int row, int col) {
        if (col < Am)
            return A.isZeroAt(row, col);
        else
            return B.isZeroAt(row, col - Am);
    }

    public boolean equals(Object obj) {
        if (obj instanceof TwoByColumnMatrixElement)
            return isEqual((Element) obj);
        return super.equals(obj);
    }

}