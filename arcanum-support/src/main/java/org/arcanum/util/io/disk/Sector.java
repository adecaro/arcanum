package org.arcanum.util.io.disk;

import java.nio.ByteBuffer;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface Sector {

    enum Mode {INIT, READ}


    int getLengthInBytes();

    Sector mapTo(Mode mode, ByteBuffer buffer);

}
