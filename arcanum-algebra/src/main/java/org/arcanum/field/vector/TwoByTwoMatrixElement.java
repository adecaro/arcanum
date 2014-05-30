package org.arcanum.field.vector;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TwoByTwoMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected AbstractMatrixElement A, B, C, D;
    protected int An, Am;


    public TwoByTwoMatrixElement(MatrixField field,
                                 AbstractMatrixElement A, AbstractMatrixElement B,
                                 AbstractMatrixElement C, AbstractMatrixElement D) {
        super(field);

        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;

        this.An = A.getN();
        this.Am = A.getM();
    }


    public final E getAt(int row, int col) {
        if (col < Am) {
            if (row < An)
                return (E) A.getAt(row, col);
            else
                return (E) C.getAt(row - An, col);
        } else {
            if (row < An)
                return (E) B.getAt(row, col - Am);
            else
                return (E) D.getAt(row - An, col - Am);
        }
    }

    public final boolean isZeroAt(int row, int col) {
        if (col < Am) {
            if (row < An)
                return A.isZeroAt(row, col);
            else
                return C.isZeroAt(row - An, col);
        } else {
            if (row < An)
                return B.isZeroAt(row, col - Am);
            else
                return D.isZeroAt(row - An, col - Am);
        }
    }

}
