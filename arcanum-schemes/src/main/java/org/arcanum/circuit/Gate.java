package org.arcanum.circuit;

/**
* @author Angelo De Caro (arcanumlib@gmail.com)
*/
public interface Gate<V> {


    public static enum Type {INPUT, AND, OR, NOT, INV, XOR, NAND}


    Type getType();

    int getIndex();

    int getDepth();

    int getNumInputs();

    int getInputIndexAt(int index);

    Gate<V> getInputAt(int index);


    V get();

    Gate<V> set(V value);

    Gate<V> evaluate();


    Gate<V> putAt(int index, V value);

    V getAt(int index);

}
