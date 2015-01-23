package org.arcanum;

import java.math.BigInteger;

/**
 * This element represents a vector through its coordinates.
 * TODO: finish javadocs.
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 * @see Element
 * @see Polynomial
 * @see Point
 */
public interface Vector<E extends Element> extends Element {


    Vector duplicate();

    Field getTargetField();

    /**
     * Returns the size of this vector.
     *
     * @return the size of this vector.
     * @since 1.0.0
     */
    int getSize();

    boolean isZeroAt(int index);

    /**
     * Returns the element at the specified coordinate.
     *
     * @param index the index of the requested coordinate.
     * @return the element at the specified coordinate.
     * @since 1.0.0
     */
    E getAt(int index);


    Vector<E> setAt(int index, E element);

    Vector<E> setAt(int index, BigInteger value);

    /**
     *
     * @param index
     * @return
     * @since 1.0.0
     */
    Vector<E> setZeroAt(int index);

    /**
     *
     * @param index
     * @return
     * @since 1.0.0
     */
    Vector<E> add(Element... es);

    @Override
    Vector<E> negate();

    Vector<E> add(Sampler<BigInteger> sampler);

    /**
     *
     * @param reader
     * @return
     */
    Vector<E> add(VectorReader<BigInteger> reader);

    /**
     *
     * @param index
     * @return
     * @since 1.0.0
     */
    Vector<E> subVectorTo(int end);

    /**
     *
     * @param index
     * @return
     * @since 1.0.0
     */
    Vector<E> subVectorFrom(int start);


    public static interface VectorReader<E> {

        int getSize();

        E getAt(int index);

    }


}
