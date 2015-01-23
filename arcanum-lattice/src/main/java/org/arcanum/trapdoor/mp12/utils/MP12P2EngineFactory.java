package org.arcanum.trapdoor.mp12.utils;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.cipher.params.ElementKeyPairParameters;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.VectorField;
import org.arcanum.trapdoor.mp12.engines.MP12ErrorTolerantOneTimePad;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2MatrixLeftSampler;
import org.arcanum.trapdoor.mp12.engines.MP12PLP2MatrixSolver;
import org.arcanum.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.generators.MP12PLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.*;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12P2EngineFactory extends MP12EngineFactory {

    private SecureRandom random;
    private int n, k;

    private MP12HLP2PublicKeyParameters latticePk;
    private MP12PLP2PublicKeyParameters primitiveLatticePk;

    private Field publicField;
    private VectorField secretField, randomnessField;
    private MatrixField oneMinusOneField;


    public MP12P2EngineFactory(SecureRandom random, int n, int k) {
        this.random = random;
        this.n = n;
        this.k = k;
    }


    @Override
    public MP12HLP2PrivateKeyParameters init() {
        // Generate trapdoor
        MP12HLP2KeyPairGenerator gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(random, n, k));
        ElementKeyPairParameters keyPair = gen.generateKeyPair();
        this.latticePk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();

        // Generate primitive trapdoor
        // Generate primitive trapdoor
        MP12PLP2KeyPairGenerator primitiveGen = new MP12PLP2KeyPairGenerator();
        primitiveGen.init(new MP12PLP2KeyPairGenerationParameters(random, n, k,
                latticePk.getM() - (n * k)
        ));
        ElementKeyPairParameters primitiveKeyPair = primitiveGen.generateKeyPair();
        this.primitiveLatticePk = (MP12PLP2PublicKeyParameters) primitiveKeyPair.getPublic();

        this.publicField = latticePk.getA().getField();
        this.secretField = new VectorField<Field>(random, primitiveLatticePk.getZq(), latticePk.getParameters().getN());
        this.randomnessField = new VectorField<Field>(random, primitiveLatticePk.getZq(), latticePk.getM());
        this.oneMinusOneField = new MatrixField<Field>(random, primitiveLatticePk.getZq(), latticePk.getM());

        return (MP12HLP2PrivateKeyParameters) keyPair.getPrivate();
    }


    @Override
    public MP12PLPublicKeyParameters getPrimitiveLatticePk() {
        return primitiveLatticePk;
    }

    @Override
    public MP12HLPublicKeyParameters getLatticePk() {
        return latticePk;
    }

    @Override
    public Field getPublicField() {
        return publicField;
    }

    public VectorField getSecretField() {
        return secretField;
    }

    public VectorField getRandomnessField() {
        return randomnessField;
    }

    public MatrixField getOneMinusOneField() {
        return oneMinusOneField;
    }

    @Override
    public int getKeyLengthInBytes() {
        return latticePk.getmInBytes();
    }


    @Override
    public ElementCipher<Element, Matrix, ElementCipherParameters> newPrimitiveLatticeSolver() {
        return new MP12PLP2MatrixSolver().init(primitiveLatticePk);
    }

    @Override
    public ElementCipher<Element, Element, ElementCipherParameters> newErrorTolerantOneTimePad() {
        return new MP12ErrorTolerantOneTimePad();
    }

    @Override
    public ElementCipher<Element, Matrix, ElementCipherParameters> newPrimitiveMatrixSolver() {
        return new MP12PLP2MatrixSolver().init(primitiveLatticePk);
    }

    @Override
    public ElementCipher createMatrixLeftSampler(ElementCipherParameters latticeSk) {
        return new MP12HLP2MatrixLeftSampler().init(new MP12HLP2SampleLeftParameters(
                        latticePk,
                        latticeSk,
                        latticePk.getM()
                )
        );
    }

}
