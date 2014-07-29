package org.arcanum.program.assignment;

import org.arcanum.program.Assignment;

import java.util.Arrays;

/**
* @author Angelo De Caro (arcanumlib@gmail.com)
*/
public class BooleanAssignment implements Assignment<Boolean> {

    private Boolean[] assignment;


    public BooleanAssignment(Boolean[] assignment) {
        this.assignment = assignment;
    }

    public BooleanAssignment(int length) {
        this.assignment = new Boolean[length];
    }

    public int getLength() {
        return assignment.length;
    }

    public Boolean getAt(int index) {
        return assignment[index];
    }

    public String toString() {
        return Arrays.toString(assignment);
    }

    public BooleanAssignment setToRandom() {
        for (int i = 0; i < assignment.length; i++) {
            assignment[i] = (Math.random() > 0.5d);
        }
        
        return this;
    }
}
