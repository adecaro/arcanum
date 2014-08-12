package org.arcanum.common.io.disk;

import org.arcanum.common.io.ByteBufferDataInput;
import org.arcanum.common.io.ByteBufferDataOutput;
import org.arcanum.common.io.ExDataInput;
import org.arcanum.common.io.ExDataOutput;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class ByteBufferBigIntegerArraySector implements ArraySector<BigInteger> {

    protected ByteBuffer buffer;
    protected int offset, recordSize, recordLength, numRecords;
    protected int lengthInBytes;

    protected ExDataInput in;
    protected ExDataOutput out;

    protected Map<String, Integer> labelsMap;



    public ByteBufferBigIntegerArraySector(int recordSize, int numRecords) throws IOException {
        this.lengthInBytes = 4 + ((recordSize + 4) * numRecords);

        this.offset = 4;
        this.recordSize = recordSize;
        this.recordLength = recordSize + 4;
        this.numRecords = numRecords;
    }

    public ByteBufferBigIntegerArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        this(recordSize, numRecords);

        labelsMap = new HashMap<String, Integer>(labels.length);
        for (int i = 0; i < labels.length; i++) {
            labelsMap.put(labels[i], i);
        }
    }


    public int getLengthInBytes() {
        return lengthInBytes;
    }

    public int getSize() {
        return numRecords;
    }

    public synchronized ArraySector<BigInteger> mapTo(Mode mode, ByteBuffer buffer) {
        this.buffer = buffer;
        this.in = new ExDataInput(new ByteBufferDataInput(buffer));
        this.out = new ExDataOutput(new ByteBufferDataOutput(buffer));

        switch (mode) {
            case INIT:
                try {
                    out.writeInt(numRecords);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case READ:
                break;
            default:
                throw new IllegalStateException("Invalid mode!");
        }

        return this;
    }

    public synchronized BigInteger getAt(int index) {
        try {
            buffer.position(offset + (index * recordLength));
            return in.readBigInteger();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setAt(int index, BigInteger value) {
        try {
            buffer.position(offset + (index * recordLength));
            out.writeBigInteger(value, recordSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BigInteger getAt(String label) {
        if (labelsMap == null)
            throw new IllegalStateException();

        return getAt(labelsMap.get(label));
    }

    public void setAt(String label, BigInteger value) {
        if (labelsMap == null)
            throw new IllegalStateException();

        setAt(labelsMap.get(label), value);
    }
}
