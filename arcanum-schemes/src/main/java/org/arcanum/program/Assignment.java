package org.arcanum.program;

/**
* @author Angelo De Caro (arcanumlib@gmail.com)
*/
public interface Assignment<E> {

    public int getLength();

    public E getAt(int index);

}
