package org.arcanum.program;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface Program {

    Element evaluate(Element... inputs);

}
