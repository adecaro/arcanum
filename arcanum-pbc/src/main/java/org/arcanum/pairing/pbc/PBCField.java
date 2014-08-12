package org.arcanum.pairing.pbc;

import com.sun.jna.Pointer;
import org.arcanum.Element;
import org.arcanum.ElementPowPreProcessing;
import org.arcanum.common.math.BigIntegerUtils;
import org.arcanum.field.base.AbstractField;
import org.arcanum.pairing.pbc.wrapper.jna.MPZElementType;
import org.arcanum.pairing.pbc.wrapper.jna.PBCElementType;
import org.arcanum.pairing.pbc.wrapper.jna.PBCPairingType;
import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class PBCField extends AbstractField {
    protected PBCPairingType pairing;
    protected PBCElementType baseElement;

    protected int fixedLengthInBytes;
    protected BigInteger order;


    protected PBCField(PBCPairingType pairing) {
        super(null);
        this.pairing = pairing;

        PBCElement temp = (PBCElement) newElement();
        MPZElementType mpzOrder = new MPZElementType();
        mpzOrder.init();
        WrapperLibraryProvider.getWrapperLibrary().pbc_field_order(temp.value, mpzOrder);

        this.order = new BigInteger(mpzOrder.toString(10));
        this.fixedLengthInBytes = WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(temp.value);
    }

    protected PBCField(PBCElementType baseElement) {
        super(null);
        this.baseElement = baseElement;

        MPZElementType mpzOrder = new MPZElementType();
        mpzOrder.init();
        WrapperLibraryProvider.getWrapperLibrary().pbc_field_order(baseElement, mpzOrder);

        this.order = new BigInteger(mpzOrder.toString(10));
        this.fixedLengthInBytes = WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(baseElement);
    }

    public BigInteger getOrder() {
        return order;
    }

    public boolean isOrderOdd() {
        return BigIntegerUtils.isOdd(order);
    }

    public Element getNqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }

    public Element[] twice(Element[] elements) {
        Pointer[] ins = new Pointer[elements.length];
        for (int i = 0; i < elements.length; i++) {
            ins[i] = ((PBCElement) elements[i]).getValue();
        }

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_multi_double2(ins, elements.length);

        return elements;
    }

    public Element[] add(Element[] a, Element[] b) {
        Pointer[] aPointers = new Pointer[a.length];
        for (int i = 0; i <a.length; i++) {
            aPointers[i] = ((PBCElement) a[i]).getValue();
        }

        Pointer[] bPointers = new Pointer[b.length];
        for (int i = 0; i <b.length; i++) {
            bPointers[i] = ((PBCElement) b[i]).getValue();
        }

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_multi_add(aPointers, aPointers, bPointers, a.length);

        return a;
    }

    public ElementPowPreProcessing getElementPowPreProcessingFromBytes(byte[] source) {
        return new PBCElementPowPreProcessing(this, source, 0);
    }

    public ElementPowPreProcessing getElementPowPreProcessingFromBytes(byte[] source, int offset) {
        return new PBCElementPowPreProcessing(this, source, offset);
    }

}
