package org.arcanum.fhe.gsw14.field;

import org.arcanum.Element;
import org.arcanum.program.ElementBranchingProgram;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14GSWBranchingProgram extends ElementBranchingProgram {

    @Override
    public boolean evaluate(boolean... inputs) {
        // 1. Initialization

        // init stat
        int[] state = new int[5];
        state[0] = 1;
        state[1] = 0;
        state[2] = 0;
        state[3] = 0;
        state[4] = 0;

        // compute complements

        int[] complements = new int[inputs.length];
        int[] ins = new int[inputs.length];


        for (int i = 0; i < complements.length; i++) {
            complements[i] = (inputs[i]) ? 0 : 1;
            ins[i] = (inputs[i]) ? 1 : 0;
        }

        // 2. Evaluation
        for (int t = 0, len = vars.size(); t < len; t++) {

            int[] newState = new int[5];

            for (int j = 0; j < 5; j++) {
                newState[j] =
                        (complements[vars.get(t)] * state[leftPerms.get(t).reverse().permute(j)])
                                +
                        (ins[vars.get(t)] * state[rightPerms.get(t).reverse().permute(j)])
                ;

            }

//            System.out.println("new state = " + Arrays.toString(newState));
//            System.out.println("newState[0] = " + newState[0]);

            System.arraycopy(newState, 0, state, 0, 5);

//            System.out.println("state     = " + Arrays.toString(state));
        }


        // 3. Output

        return (state[0] == 1) ? true : false;
    }

    public Element evaluate(Element... inputs) {
        // 1. Initialization
        AP14GSW14Field field = (AP14GSW14Field) inputs[0].getField();

        // init stat
        Element[] state = new Element[5];
        state[0] = field.newElementErrorFree(1);
        state[1] = field.newElementErrorFree(0);
        state[2] = field.newElementErrorFree(0);
        state[3] = field.newElementErrorFree(0);
        state[4] = field.newElementErrorFree(0);

        // compute complements
        Element[] complements = new Element[inputs.length];
        for (int i = 0; i < complements.length; i++) {
            // TODO: encrypt 1 without error!
            complements[i] = field.newOneElement().sub(inputs[i]);
        }

        // 2. Evaluation
        Element[] newState = new Element[5];
        Element b = field.newZeroElement();
        for (int t = 0, len = vars.size(); t < len; t++) {

            for (int j = 0; j < 5; j++) {
                // TODO: can we avoid duplication?
                Element a = complements[vars.get(t)].duplicate().mul(state[leftPerms.get(t).reverse().permute(j)]);
                b.set(inputs[vars.get(t)]).mul(state[rightPerms.get(t).reverse().permute(j)]);

                newState[j] = a.add(b);
            }

            // Copy newState to state
            System.arraycopy(newState, 0, state, 0, 5);
        }

        // 3. Output
        return state[0];
    }


}
