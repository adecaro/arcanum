package org.arcanum.trapdoor.mp12.utils;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.VectorField;
import org.arcanum.trapdoor.mp12.params.MP12HLPublicKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLPublicKeyParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class MP12EngineFactory {


    public abstract ElementCipherParameters init();


    public abstract MP12PLPublicKeyParameters getPrimitiveLatticePk();

    public abstract MP12HLPublicKeyParameters getLatticePk();

    public abstract Field getPublicField();

    public abstract VectorField getSecretField();

    public abstract VectorField getRandomnessField();

    public abstract MatrixField getOneMinusOneField();


    public abstract ElementCipher<Element, Matrix, ElementCipherParameters> newPrimitiveLatticeSolver();

    public abstract ElementCipher<Element, Element, ElementCipherParameters> newErrorTolerantOneTimePad();

    public abstract ElementCipher<Element, Matrix, ElementCipherParameters> newPrimitiveMatrixSolver();

    public abstract ElementCipher createMatrixLeftSampler(ElementCipherParameters latticeSk);


    public abstract int getKeyLengthInBytes();
}
