package org.arcanum.pairing.pbc;

import com.sun.jna.Pointer;
import org.arcanum.Element;
import org.arcanum.ElementPowPreProcessing;
import org.arcanum.Field;
import org.arcanum.pairing.pbc.wrapper.jna.MPZElementType;
import org.arcanum.pairing.pbc.wrapper.jna.PBCElementPPType;
import org.arcanum.pairing.pbc.wrapper.jna.WrapperLibraryProvider;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCElementPowPreProcessing implements ElementPowPreProcessing {
    protected Field field;
    protected PBCElementPPType elementPPType;

    protected Pointer element;


    public PBCElementPowPreProcessing(Field field, Pointer element) {
        this.field = field;
        this.elementPPType = new PBCElementPPType(element);

        this.element = element;
    }

    public PBCElementPowPreProcessing(Field field, byte[] source, int offset) {
        this.field = field;

        fromBytes(source, offset);
    }


    public Field getField() {
        return field;
    }

    public Element pow(BigInteger n) {
        PBCElement result = (PBCElement) field.newElement();
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_pow(result.value, MPZElementType.fromBigInteger(n), elementPPType);

        return result;
    }

    public Element powZn(Element n) {
        PBCElement result = (PBCElement) field.newElement();
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_pow_zn(result.value, ((PBCElement) n).value, elementPPType);

        return result;
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(element)];
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_to_bytes(bytes, element);
        return bytes;
    }

    public void fromBytes(byte[] source, int offset) {
        PBCElement temp = (PBCElement) field.newElementFromBytes(source, offset);

        this.element = temp.value;
        this.elementPPType = new PBCElementPPType(this.element);
    }
}
