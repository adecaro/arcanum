package org.arcanum.util.io.disk;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface MatrixSector<T> extends Sector {

    int getN();

    int getM();

    T getAt(int i, int j);

    void setAt(int i, int j, T value);

}
