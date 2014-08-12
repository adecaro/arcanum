package org.arcanum.fhe.gsw14.program;

import org.arcanum.Element;
import org.arcanum.common.concurrent.ExecutorServiceUtils;
import org.arcanum.common.concurrent.PoolExecutor;
import org.arcanum.fhe.gsw14.field.AP14GSW14Field;
import org.arcanum.program.AbstractProgramEvaluator;
import org.arcanum.program.Assignment;
import org.arcanum.program.assignment.ElementAssignment;
import org.arcanum.program.pbp.PermutationBranchingProgram;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14GSWPBPEvaluator extends AbstractProgramEvaluator<PermutationBranchingProgram, Element, Element> {

    public Element evaluate(PermutationBranchingProgram pbp, Element... inputs) {
        return evaluate(pbp, new ElementAssignment(inputs));
    }

    public Element evaluate(final PermutationBranchingProgram pbp, final Assignment<Element> assignment) {
        // 1. Initialization
        AP14GSW14Field field = (AP14GSW14Field) assignment.getAt(0).getField();

        // init stat
        final Element[] state = new Element[5];
        state[0] = field.newElementErrorFreeFullMatrix(1);
        state[1] = field.newElementErrorFreeFullMatrix(0);
        state[2] = field.newElementErrorFreeFullMatrix(0);
        state[3] = field.newElementErrorFreeFullMatrix(0);
        state[4] = field.newElementErrorFreeFullMatrix(0);

        // compute complements
        final Element[] complements = new Element[assignment.getLength()];
        for (int i = 0; i < complements.length; i++)
            complements[i] = field.newElementErrorFreeFullMatrix(1).sub(assignment.getAt(i));

        // 2. Evaluation
        final Element[] newState = new Element[5];
        for (int i = 0; i < 5; i++)
            newState[i] = field.newEmptyElement();

        final Element b[] = new Element[5];

        for (int i = 0; i < 5; i++)
            b[i] = field.newEmptyElement();


        System.out.printf("%s - %s - %s - %s - %s\n",
                state[0].toBigInteger(),
                state[1].toBigInteger(),
                state[2].toBigInteger(),
                state[3].toBigInteger(),
                state[4].toBigInteger()
        );

        for (int i = 0, len = pbp.getLength(); i < len; i++) {
            System.out.printf("Step %d...\n", i);

            long start = System.currentTimeMillis();

            PoolExecutor executor = new PoolExecutor();

            for (int j = 0; j < 5; j++) {
//                Element a = complements[vars.get(i)].duplicate().mul(state[leftPerms.get(i).getInverse().permute(j)]);
//                inputs[vars.get(i)].mulTo(state[rightPerms.get(i).getInverse().permute(j)], b);
//                newState[j] = a.add(b);

                executor.submit(new ExecutorServiceUtils.IndexRunnable(i, j) {
                    public void run() {
                        long start1 = System.currentTimeMillis();

                        complements[pbp.getVarIndexAt(i)].mulTo(state[pbp.permuteLeftInverseAt(i, j)], newState[j]);
                        assignment.getAt(pbp.getVarIndexAt(i)).mulTo(state[pbp.permuteRightInverseAt(i, j)], b[j]);

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

            System.out.printf("%s - %s - %s - %s - %s\n",
                    state[0].toBigInteger(),
                    state[1].toBigInteger(),
                    state[2].toBigInteger(),
                    state[3].toBigInteger(),
                    state[4].toBigInteger()
            );

            System.out.printf("Step %d completed in millis %d\n", i, (end-start));


        }

        // 3. Output
        System.out.printf("%s - %s - %s - %s - %s\n",
                state[0].toBigInteger(),
                state[1].toBigInteger(),
                state[2].toBigInteger(),
                state[3].toBigInteger(),
                state[4].toBigInteger()
        );


        return state[0];
    }
}
