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


    boolean isSymmetric();

    boolean isSquare();

    boolean isZeroAt(int row, int col);


    E getAt(int row, int col);

    Matrix<E> setAt(int row, int col, E e);

    Matrix<E> setZeroAt(int row, int col);

    Matrix<E> setOneAt(int row, int col);


    Vector<E> getRowAt(int row);

    Vector<E> getColumnAt(int col);

    Matrix<E> setRowAt(int row, Element rowElement);

    Matrix<E> setRowsToRandom(int start, int end);

    Matrix<E> setColAt(int col, Element colElement);


    Matrix<E> setIdentityAt(int row, int col, int n);

    Matrix<E> setIdentityAt(int row, int col, int n, Element e);

    Matrix<E> setMatrixAt(int row, int col, Matrix e);

    Matrix<E> setMatrixAt(int row, int col, Element e, Transformer transformer);

    Matrix<E> setTransposeAt(int row, int col, Element e);



    Matrix<E> mulByTranspose();

    Matrix<E> mulByTransposeTo(Matrix matrix, int offsetRow, int offsetCol, Transformer transformer);

    Element mulFromTranspose(Element e);

    Element mul(ColumnReader<E> reader);

    Element mulTo(ColumnReader<E> reader, Element to);

    Matrix<E> transform(Transformer transformer);

    Matrix<E> transformDiagonal(Transformer transformer);


    Matrix<E> getViewRowsAt(int start, int end);

    Vector<E> getViewRowAt(int row);

    Vector<E> getViewColAt(int col);


    String rowsToString(int startRow, int startCol);


    public static interface Transformer {

        public void transform(int row, int col, Element e);

    }

    public static interface ColumnReader<E extends Element> {

        int getM();

        Vector<E> getColumnAt(int column);

    }

}