package org.arcanum.program.assignment;

import org.arcanum.program.Assignment;

/**
* @author Angelo De Caro (arcanumlib@gmail.com)
*/
public class CharAssignment implements Assignment<Character> {

    private String assignmentAsString;
    private Character[] assignment;


    public CharAssignment(Character[] assignment) {
        this.assignment = assignment;
    }

    public CharAssignment(int length) {
        this.assignment = new Character[length];
    }

    public CharAssignment(String assignment) {
        this.assignmentAsString = assignment;
    }


    public int getLength() {
        if (assignment == null)
            return assignmentAsString.length();

        return assignment.length;
    }

    public Character getAt(int index) {
        if (assignment == null)
            return assignmentAsString.charAt(index);

        return assignment[index];
    }

    public String toString() {
        if (assignment == null)
            return assignmentAsString;

        StringBuilder b = new StringBuilder();
        for (Character anAssignment : assignment) b.append(anAssignment);
        return b.toString();
    }

}
