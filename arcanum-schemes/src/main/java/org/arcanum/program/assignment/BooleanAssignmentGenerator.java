package org.arcanum.program.assignment;

import java.util.Iterator;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BooleanAssignmentGenerator implements Iterable<BooleanAssignment> {

    private int n, length;

    public BooleanAssignmentGenerator(int n) {
        this.n = n;
        this.length = (int) Math.pow(2, n);
    }

    public Iterator<BooleanAssignment> iterator() {
        return new Iterator<BooleanAssignment>() {
            private int cursor;

            public boolean hasNext() {
                return cursor < length;
            }

            public BooleanAssignment next() {
                Boolean[] next = new Boolean[n];
                for (int i = 0; i < n; i++) {
                    next[i] = (((cursor >>> i) & 1) != 0);
                }
                cursor++;
                return new BooleanAssignment(next);
            }

            public void remove() {
                throw new IllegalStateException("Not implemented yet!!!");
            }
        };
    }


}
