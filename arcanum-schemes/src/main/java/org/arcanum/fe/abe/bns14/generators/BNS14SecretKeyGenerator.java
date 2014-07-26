package org.arcanum.fe.abe.bns14.generators;

import org.arcanum.Element;
import org.arcanum.circuit.ArithmeticCircuit;
import org.arcanum.circuit.ArithmeticGate;
import org.arcanum.fe.abe.bns14.params.BNS14MasterSecretKeyParameters;
import org.arcanum.fe.abe.bns14.params.BNS14PublicKeyParameters;
import org.arcanum.fe.abe.bns14.params.BNS14SecretKeyGenerationParameters;
import org.arcanum.fe.abe.bns14.params.BNS14SecretKeyParameters;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2MatrixLeftSampler;
import org.arcanum.trapdoor.mp12.engines.MP12PLP2MatrixSolver;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleLeftParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14SecretKeyGenerator {
    private BNS14SecretKeyGenerationParameters param;

    private ArithmeticCircuit circuit;

    public BNS14SecretKeyGenerator init(KeyGenerationParameters param) {
        this.param = (BNS14SecretKeyGenerationParameters) param;

        this.circuit = this.param.getCircuit();

        return this;
    }

    public CipherParameters generateKey() {
        BNS14MasterSecretKeyParameters msk = param.getMasterSecretKeyParameters();
        BNS14PublicKeyParameters pk = param.getPublicKeyParameters();

        ArithmeticCircuit circuit = this.circuit;

        // encode the circuit
        Map<Integer, Element> keys = new HashMap<Integer, Element>();

        MP12PLP2MatrixSolver solver = new MP12PLP2MatrixSolver();
        solver.init(pk.getPrimitiveLatticePk());

        for (ArithmeticGate gate : circuit) {
            int index = gate.getIndex();

            switch (gate.getType()) {
                case INPUT:
                    keys.put(index, pk.getBAt(index));
                    break;

                case OR:
                    // addition
                    Element B = pk.getBAt(0).getField().newZeroElement();
                    for (int j = 0, k = gate.getNumInputs(); j < k; j++) {
                        // \alpha_i G
                        Element R = solver.processElements(
                                (gate.getAlphaAt(j).isOne()) ?
                                        pk.getPrimitiveLatticePk().getG() :
                                        pk.getPrimitiveLatticePk().getG().duplicate().mulZn(gate.getAlphaAt(j))
                        );
                        B.add(keys.get(gate.getInputIndexAt(j)).mul(R));
                    }

                    keys.put(index, B);
                    break;

                case AND:
                    // multiplication

                    // Compute R_0 = SolveR(G, T_G, \alpha G)
                    Element R = solver.processElements(
                            (gate.getAlphaAt(0).isOne()) ?
                                    pk.getPrimitiveLatticePk().getG() :
                                    pk.getPrimitiveLatticePk().getG().duplicate().mulZn(gate.getAlphaAt(0))
                    );
                    for (int j = 1, k = gate.getNumInputs(); j < k; j++) {
                        // R_j = SolveR(G, T_G, - B_{j-1} R_{j-1})
                        R = solver.processElements(keys.get(gate.getInputIndexAt(j - 1)).mul(R).negate());
                    }

                    // Compute B_g = B_{k-1} R_{k-1}
                    keys.put(index, keys.get(gate.getInputIndexAt(gate.getNumInputs() - 1)).mul(R));
                    break;
            }
        }

        circuit.getOutputGate().putAt(-1, keys.get(circuit.getOutputGate().getIndex()));

        // SampleLeft

        MP12HLP2MatrixLeftSampler sampler = new MP12HLP2MatrixLeftSampler();
        sampler.init(new MP12HLP2SampleLeftParameters(
                        pk.getLatticePk(),
                        msk.getLatticeSk(),
                        pk.getLatticePk().getM()
                )
        );

        Element skC = sampler.processElements(
                keys.get(circuit.getOutputGate().getIndex()),
                pk.getD()
        );

//        Element F = MatrixField.unionByCol(pk.getLatticePk().getA(), keys.get(circuit.getOutputGate().getIndex()));
//        Element DPrime = F.mul(skC);
//        assertTrue(DPrime.equals(pk.getD()));

        return new BNS14SecretKeyParameters(pk, circuit, skC);
    }

}