package org.arcanum.common.io.disk;

import org.arcanum.common.collection.FlagMap;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class ByteBufferLatchSoftRefBigIntegerArraySector extends ByteBufferSoftRefBigIntegerArraySector {

    protected FlagMap<Integer> flags;


    public ByteBufferLatchSoftRefBigIntegerArraySector(int recordSize, int numRecords) throws IOException {
        super(recordSize, numRecords);

        this.flags = new FlagMap<Integer>();
    }

    public ByteBufferLatchSoftRefBigIntegerArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        super(recordSize, numRecords, labels);

        this.flags = new FlagMap<Integer>();
    }


    public BigInteger getAt(int index) {
        flags.get(index);

        return super.getAt(index);
    }

    public void setAt(int index, BigInteger value) {
        super.setAt(index, value);

        flags.set(index);
    }

}
