package org.arcanum.common.io;

import org.arcanum.common.collection.Arrays;

import java.io.DataOutput;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ExDataOutput implements DataOutput {

    private DataOutput dataOutput;


    public ExDataOutput(DataOutput dataOutput) {
        this.dataOutput = dataOutput;
    }


    public void write(int b) throws IOException {
        dataOutput.write(b);
    }

    public void write(byte[] b) throws IOException {
        dataOutput.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        dataOutput.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        dataOutput.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        dataOutput.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        dataOutput.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        dataOutput.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        dataOutput.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        dataOutput.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        dataOutput.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        dataOutput.writeDouble(v);
    }

    public void writeBytes(String s) throws IOException {
        dataOutput.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        dataOutput.writeChars(s);
    }

    public void writeUTF(String s) throws IOException {
        dataOutput.writeUTF(s);
    }


    public void writeInts(int[] ints) throws IOException {
        if (ints == null) {
            writeInt(0);
        } else {
            writeInt(ints.length);
            for (int anInt : ints) writeInt(anInt);
        }
    }

    public void writeBytes(byte[] buffer) throws IOException{
        writeInt(buffer.length);
        write(buffer);
    }

    public void writeBigInteger(BigInteger bigInteger) throws IOException {
        writeBytes(bigInteger.toByteArray());
    }

    public void writeBigInteger(BigInteger bigInteger, int ensureLength) throws IOException {
        byte[] bytes = bigInteger.toByteArray();

        if (bytes.length > ensureLength) {
            // strip the zero prefix
            if (bytes[0] == 0 && bytes.length == ensureLength + 1) {
                // Remove it
                bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
            } else
                throw new IllegalStateException("result has more than allowed bytes.");
        } else if (bytes.length < ensureLength) {
            byte[] result = new byte[ensureLength];
            System.arraycopy(bytes, 0, result, ensureLength - bytes.length, bytes.length);
            bytes = result;
        }

        writeBytes(bytes);
    }

    public void writeBigIntegers(BigInteger[] bigIntegers) throws IOException {
        writeInt(bigIntegers.length);
        for (BigInteger bigInteger : bigIntegers) {
            writeBigInteger(bigInteger);
        }
    }

    public void writeBigIntegers(BigInteger[] bigIntegers, int ensureLength) throws IOException {
        writeInt(bigIntegers.length);
        for (BigInteger bigInteger : bigIntegers) {
            writeBigInteger(bigInteger, ensureLength);
        }
    }

}