package org.arcanum.fhe.gsw14.field;

import org.arcanum.Element;
import org.arcanum.program.ElementBranchingProgram;
import org.arcanum.util.concurrent.ExecutorServiceUtils;
import org.arcanum.util.concurrent.PoolExecutor;

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

        return (state[0] == 1);
    }

    public Element evaluate(final Element... inputs) {
        // 1. Initialization
        AP14GSW14Field field = (AP14GSW14Field) inputs[0].getField();

        // init stat
        final Element[] state = new Element[5];
        state[0] = field.newElementErrorFreeFullMatrix(1);
        state[1] = field.newElementErrorFreeFullMatrix(0);
        state[2] = field.newElementErrorFreeFullMatrix(0);
        state[3] = field.newElementErrorFreeFullMatrix(0);
        state[4] = field.newElementErrorFreeFullMatrix(0);

        // compute complements
        final Element[] complements = new Element[inputs.length];
        for (int i = 0; i < complements.length; i++)
            complements[i] = field.newElementErrorFreeFullMatrix(1).sub(inputs[i]);

        // 2. Evaluation
        final Element[] newState = new Element[5];
        for (int i = 0; i < 5; i++)
            newState[i] = field.newEmptyElement();

        final Element b[] = new Element[5];

        for (int i = 0; i < 5; i++)
            b[i] = field.newEmptyElement();
        for (int i = 0, len = vars.size(); i < len; i++) {
            System.out.printf("Step %d...\n", i);

            long start = System.currentTimeMillis();


            PoolExecutor executor = new PoolExecutor();

            for (int j = 0; j < 5; j++) {
//                Element a = complements[vars.get(i)].duplicate().mul(state[leftPerms.get(i).reverse().permute(j)]);
//                inputs[vars.get(i)].mulTo(state[rightPerms.get(i).reverse().permute(j)], b);
//                newState[j] = a.add(b);

                executor.submit(new ExecutorServiceUtils.IndexRunnable(i, j) {
                    public void run() {
                        long start1 = System.currentTimeMillis();

                        complements[vars.get(i)].mulTo(state[leftPerms.get(i).reverse().permute(j)], newState[j]);
                        inputs[vars.get(i)].mulTo(state[rightPerms.get(i).reverse().permute(j)], b[j]);
                        newState[j].add(b[j]);

                        long end1 = System.currentTimeMillis();
                        System.out.printf("Step %d.%d completed in millis %d\n", i, j, (end1 - start1));
                    }
                });

            }

            // Copy newState to state
//            System.arraycopy(newState, 0, state, 0, 5);

            executor.awaitTermination();

            for (int j = 0; j < 5; j++)
                state[j].swap(newState[j]);

            long end = System.currentTimeMillis();
            System.out.printf("Step %d completed in millis %d\n", i, (end-start));
        }

        // 3. Output
        return state[0];
    }


}
