package org.arcanum.program.assignment;

import org.arcanum.program.Assignment;

/**
* @author Angelo De Caro (arcanumlib@gmail.com)
*/
public class BooleanAssignment implements Assignment<Boolean> {

    private Boolean[] assignment;


    public BooleanAssignment(Boolean... assignment) {
        this.assignment = assignment;
    }

    public BooleanAssignment(int length) {
        this.assignment = new Boolean[length];
    }

    public BooleanAssignment(String assignment) {
        this.assignment = new Boolean[assignment.length()];
        for (int i = 0; i < this.assignment.length; i++)
            this.assignment[i] = assignment.charAt(i) == '1';
    }


    public int getLength() {
        return assignment.length;
    }

    public Boolean getAt(int index) {
        return assignment[index];
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Boolean anAssignment : assignment) b.append(anAssignment ? 1 : 0);
        return b.toString();
    }

    public BooleanAssignment setToRandom() {
        for (int i = 0; i < assignment.length; i++) {
            assignment[i] = (Math.random() > 0.5d);
        }
        
        return this;
    }
}
