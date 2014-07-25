package org.arcanum.circuit;

/**
* @author Angelo De Caro (arcanumlib@gmail.com)
*/
public interface Gate<V> {

    public static enum Type {INPUT, AND, OR, NOT, NAND}

    Type getType();

    int getIndex();

    int getDepth();

    int getInputIndexAt(int index);

    int getInputNum();

    Gate<V> getInputAt(int index);

    Gate<V> evaluate();

    V get();

    Gate<V> set(V value);

    Gate<V> putAt(int index, V value);

    V getAt(int index);

}
