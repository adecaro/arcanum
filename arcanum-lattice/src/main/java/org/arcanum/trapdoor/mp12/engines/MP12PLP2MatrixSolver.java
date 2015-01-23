package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.common.concurrent.ExecutorServiceUtils;
import org.arcanum.common.concurrent.PoolExecutor;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;

import java.util.concurrent.ExecutorService;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2MatrixSolver<P extends MP12PLP2PublicKeyParameters> extends AbstractElementCipher<Element, Matrix, P> {

    protected static final ExecutorService EXECUTOR_SERVICE = ExecutorServiceUtils.getNewFixedThreadPool();


    protected MatrixField outputField;
    protected MP12PLP2Solver solver;


    public MP12PLP2MatrixSolver() {
        this.solver = new MP12PLP2Solver();
    }


    @Override
    public MP12PLP2MatrixSolver<P> init(P param) {
        solver.init(param);

        return this;
    }

    @Override
    public Matrix processElements(Element... input) {
        final Matrix U = (Matrix) input[0];

        final Matrix result;
        if (outputField != null) {
            result = outputField.newElement();
        } else {
            result = new MatrixField<Field>(
                    solver.parameters.getParameters().getRandom(),
                    solver.parameters.getZq(),
                    solver.parameters.getPreimageField().getN(), U.getM()
            ).newElement();
        }

        PoolExecutor pool = new PoolExecutor(EXECUTOR_SERVICE);
        for (int i = 0, length = U.getM(); i < length; i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
//                    result.setColAt(finalI, MP12PLP2MatrixSolver.super.processElements(U.getColumnAt(finalI)));
                    solver.processElementsTo(
                            result.getViewColAt(finalI),
                            U.getViewColAt(finalI)
                    );
                }
            });
        }
        pool.awaitTermination();

        return result;
    }
}
