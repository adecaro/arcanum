package org.arcanum.circuit;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface ArithmeticGate extends Gate<Element> {

    Element getAlphaAt(int index);

}
