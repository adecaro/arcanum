package org.arcanum;

/**
 * This element represents a polynomial through its coefficients.
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 * @see Element
 * @see Vector
 */
public interface Polynomial<E extends Element> extends Element, Vector {

    /**
     * Returns the degree of this polynomial.
     *
     * @return the degree of this polynomial.
     * @since 1.0.0
     */
    int getDegree();

}
