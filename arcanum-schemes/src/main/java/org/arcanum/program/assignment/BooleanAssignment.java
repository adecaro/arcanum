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

    public int getLength() {
        return assignment.length;
    }

    public Boolean getAt(int index) {
        return assignment[index];
    }


    @Override
    public String toString() {
        return Arrays.toString(assignment);
    }
}
