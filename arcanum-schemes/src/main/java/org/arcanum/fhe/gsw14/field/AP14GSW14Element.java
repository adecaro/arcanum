package org.arcanum.fhe.gsw14.field;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Vector;
import org.arcanum.field.base.AbstractElement;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14GSW14Element extends AbstractElement<AP14GSW14Field> implements Matrix {

    protected Matrix value;


    public AP14GSW14Element(AP14GSW14Field field, Matrix value) {
        super(field);

        this.value = value;
    }


    public Element duplicate() {
        return new AP14GSW14Element(field, (Matrix) value.duplicate());
    }

    public Element set(Element value) {
        this.value.set(((AP14GSW14Element) value).value);

        return this;
    }

    public Element set(int value) {
        return null;
    }

    public Element set(BigInteger value) {
        return null;
    }

    public BigInteger toBigInteger() {
        return field.decrypt(value);
    }

    public Element setToRandom() {
        return null;
    }

    public Element setFromHash(byte[] source, int offset, int length) {
        return null;
    }

    public Element setToZero() {
        return null;
    }

    public boolean isZero() {
        return false;
    }

    public Element setToOne() {
        return null;
    }

    public boolean isEqual(Element value) {
        return false;
    }

    public boolean isOne() {
        return false;
    }

    public Element invert() {
        return null;
    }

    public Element negate() {
        return null;
    }

    public Element add(Element element) {
        this.value.add(((AP14GSW14Element) element).value);

        return this;
    }

    public Element sub(Element element) {
        this.value.sub(((AP14GSW14Element) element).value);

        return this;
    }

    public Element mul(Element element) {
        this.value = (Matrix) this.value.mul(
                field.gInvert(((AP14GSW14Element) element).value)
        );

        return this;
    }

    public Element mul(BigInteger n) {
        return null;
    }

    public boolean isSqr() {
        return false;
    }

    public int sign() {
        return 0;
    }


    public Field getTargetField() {
        return value.getTargetField();
    }

    public int getN() {
        return value.getN();
    }

    public int getM() {
        return value.getM();
    }

    public boolean isSymmetric() {
        return value.isSymmetric();
    }

    public boolean isSquare() {
        return value.isSquare();
    }

    public boolean isZeroAt(int row, int col) {
        return value.isZeroAt(row, col);
    }

    public Element getAt(int row, int col) {
        return value.getAt(row, col);
    }

    public Matrix setAt(int row, int col, Element element) {
        return value.setAt(row, col, element);
    }

    public Matrix setZeroAt(int row, int col) {
        return value.setZeroAt(row, col);
    }

    public Matrix setOneAt(int row, int col) {
        return value.setOneAt(row, col);
    }

    public Vector getRowAt(int row) {
        return value.getRowAt(row);
    }

    public Vector getColumnAt(int col) {
        return value.getColumnAt(col);
    }

    public Matrix setRowAt(int row, Element rowElement) {
        return value.setRowAt(row, rowElement);
    }

    public Matrix setRowsToRandom(int start, int end) {
        return value.setRowsToRandom(start, end);
    }

    public Matrix setColAt(int col, Element colElement) {
        return value.setColAt(col, colElement);
    }

    public Matrix setIdentityAt(int row, int col, int n) {
        return value.setIdentityAt(row, col, n);
    }

    public Matrix setIdentityAt(int row, int col, int n, Element e) {
        return value.setIdentityAt(row, col, n, e);
    }

    public Matrix setMatrixAt(int row, int col, Matrix e) {
        return value.setMatrixAt(row, col, e);
    }

    public Matrix setMatrixAt(int row, int col, Element e, Transformer transformer) {
        return value.setMatrixAt(row, col, e, transformer);
    }

    public Matrix setTransposeAt(int row, int col, Element e) {
        return value.setTransposeAt(row, col, e);
    }

    public Matrix mulByTranspose() {
        return value.mulByTranspose();
    }

    public Matrix mulByTransposeTo(Matrix matrix, int offsetRow, int offsetCol, Transformer transformer) {
        return value.mulByTransposeTo(matrix, offsetRow, offsetCol, transformer);
    }

    public Element mulFromTranspose(Element e) {
        return value.mulFromTranspose(e);
    }

    public Element mulTo(Element e, Element to) {
        return value.mulTo(e, to);
    }

    public Element mul(ColumnReader reader) {
        return value.mul(reader);
    }

    public Matrix transform(Transformer transformer) {
        return value.transform(transformer);
    }

    public Matrix transformDiagonal(Transformer transformer) {
        return value.transformDiagonal(transformer);
    }

    public Matrix getViewRowsAt(int start, int end) {
        return value.getViewRowsAt(start, end);
    }

    public Vector getViewRowAt(int row) {
        return value.getViewRowAt(row);
    }

    public Vector getViewColAt(int col) {
        return value.getViewColAt(col);
    }

    public String rowsToString(int startRow, int startCol) {
        return value.rowsToString(startRow, startCol);
    }
}
