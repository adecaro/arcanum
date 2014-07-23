package org.arcanum.pairing.pbc;

import org.arcanum.Element;
import org.arcanum.Point;
import org.arcanum.Vector;
import org.arcanum.pairing.pbc.wrapper.jna.PBCElementType;
import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;
import org.arcanum.util.Arrays;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCCurvePointElement extends PBCElement implements Point {

    public PBCCurvePointElement(PBCElementType value, PBCField field) {
        super(value, field);
    }

    public PBCCurvePointElement(PBCElement pbcElement) {
        super(pbcElement);
    }

    @Override
    public PBCElement getImmutable() {
        return new ImmutablePBCCurvePointElement(this);
    }

    public int getSize() {
        return 2;
    }

    public Element getAt(int index) {
        return (index == 0) ? getX() : getY();
    }

    public Element getX() {
        PBCElementType dest = new PBCElementType();
        WrapperLibraryProvider.getWrapperLibrary().pbc_curve_x_coord(dest, this.value);

        return new PBCElement(dest, new PBCFieldFromElement(dest));
    }

    public Element getY() {
        PBCElementType dest = new PBCElementType();
        WrapperLibraryProvider.getWrapperLibrary().pbc_curve_y_coord(dest, this.value);

        return new PBCElement(dest, new PBCFieldFromElement(dest));
    }

    public int getLengthInBytesCompressed() {
        return WrapperLibraryProvider.getWrapperLibrary().element_length_in_bytes_compressed(this.value);
    }

    public byte[] toBytesCompressed() {
        byte[] bytes = new byte[WrapperLibraryProvider.getWrapperLibrary().element_length_in_bytes_compressed(value)];
        WrapperLibraryProvider.getWrapperLibrary().element_to_bytes_compressed(bytes, value);
        return bytes;
    }

    public int setFromBytesCompressed(byte[] source) {
        return setFromBytesCompressed(source, 0);
    }

    public int setFromBytesCompressed(byte[] source, int offset) {
        int lengthInBytes = WrapperLibraryProvider.getWrapperLibrary().element_length_in_bytes_compressed(value);
        WrapperLibraryProvider.getWrapperLibrary().element_from_bytes_compressed(value, Arrays.copyOf(source, offset, lengthInBytes));
        return lengthInBytes;
    }

    public int getLengthInBytesX() {
        return WrapperLibraryProvider.getWrapperLibrary().element_length_in_bytes_x_only(this.value);
    }

    public byte[] toBytesX() {
        byte[] bytes = new byte[WrapperLibraryProvider.getWrapperLibrary().element_length_in_bytes_x_only(value)];
        WrapperLibraryProvider.getWrapperLibrary().element_to_bytes_x_only(bytes, value);
        return bytes;
    }

    public int setFromBytesX(byte[] source) {
        return setFromBytesX(source, 0);
    }

    public int setFromBytesX(byte[] source, int offset) {
        int lengthInBytes = WrapperLibraryProvider.getWrapperLibrary().element_length_in_bytes_x_only(value);
        WrapperLibraryProvider.getWrapperLibrary().element_from_bytes_x_only(value, Arrays.copyOf(source, offset, lengthInBytes));
        return lengthInBytes;
    }

    public Element setFromObject(Object value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Vector subVectorTo(int end) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Vector subVectorFrom(int start) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Vector setZeroAt(int index) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Vector add(Element... es) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    @Override
    public PBCElement addProduct(Element at, Element b) {
        throw new IllegalStateException("Not implemented yet!!!");
    }
}