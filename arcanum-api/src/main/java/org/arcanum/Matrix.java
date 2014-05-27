package org.arcanum;

/**
 * TODO:
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @see Element
 * @since 1.0.0
 */
public interface Matrix <E extends Element> extends Element {

    Field getTargetField();

    int getN();

    int getM();

    E getAt(int row, int col);

    Matrix<E> setAt(int row, int col, E e);

    Matrix<E> setZeroAt(int row, int col);

    Matrix<E> setOneAt(int row, int col);


    Vector<E> rowAt(int row);

    Vector<E> columnAt(int col);

    Matrix<E> setRowAt(int row, Element rowElement);

    Matrix<E> setColAt(int col, Element colElement);


    Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n);

    Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n, Element e);

    Matrix<E> setSubMatrixFromMatrixAt(int row, int col, Element e);

    Matrix<E> setSubMatrixFromMatrixAt(int row, int col, Element e, Transformer transformer);

    Matrix<E> setSubMatrixFromMatrixTransposeAt(int row, int col, Element e);

    Matrix<E> mulByTranspose();

    Matrix<E> mulByTransposeTo(Matrix matrix, int offsetRow, int offsetCol, Transformer transformer);

    Element mulFromTranspose(Element e);

    Matrix<E> transform(Transformer transformer);


    public boolean isSymmetric();

    public boolean isSquare();

    public boolean isZeroAt(int row, int col);


    String toStringSubMatrix(int startRow, int startCol);


    public static interface Transformer {

        public void transform(int row, int col, Element e);

    }
}