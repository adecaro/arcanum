package org.arcanum.fe.abe.gghsw13.generators;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.BooleanGate;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13MasterSecretKeyParameters;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13PublicKeyParameters;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13SecretKeyGenerationParameters;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13SecretKeyParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13SecretKeyGenerator {
    private GGHSW13SecretKeyGenerationParameters param;

    private Pairing pairing;
    private BooleanCircuit circuit;

    public void init(KeyGenerationParameters param) {
        this.param = (GGHSW13SecretKeyGenerationParameters) param;

        this.pairing = this.param.getMasterSecretKeyParameters().getParameters().getPairing();
        this.circuit = this.param.getCircuit();
    }

    public CipherParameters generateKey() {
        GGHSW13MasterSecretKeyParameters msk = param.getMasterSecretKeyParameters();
        GGHSW13PublicKeyParameters pk = param.getPublicKeyParameters();

        BooleanCircuit circuit = this.circuit;

        // sample the randomness
        Element[] rs = new Element[circuit.getN() + circuit.getQ()];
        for (int i = 0; i < rs.length; i++)
            rs[i] = pairing.getZr().newRandomElement().getImmutable();

        // encode the circuit

        Map<Integer, Element[]> keys = new HashMap<Integer, Element[]>();

        Element ePrime = pairing.getFieldAt(circuit.getDepth()).newElement().powZn(msk.getAlpha().sub(rs[rs.length - 1]));
        keys.put(-1, new Element[]{ePrime});

        for (BooleanGate gate : circuit) {
            int index = gate.getIndex();
            int depth = gate.getDepth();

            switch (gate.getType()) {
                case INPUT:

                    Element z = pairing.getZr().newRandomElement();

                    Element e1 = pairing.getG1().newElement().powZn(rs[index]).mul(pk.getHAt(index).powZn(z));
                    Element e2 = pairing.getG1().newElement().powZn(z.negate());

                    keys.put(index, new Element[]{e1, e2});

                    break;

                case OR:
                    Element a = pairing.getZr().newRandomElement();
                    Element b = pairing.getZr().newRandomElement();

                    e1 = pairing.getG1().newElement().powZn(a);
                    e2 = pairing.getG1().newElement().powZn(b);

                    Element e3 = pairing.getFieldAt(depth).newElement().powZn(
                            rs[index].sub(a.mul(rs[gate.getInputIndexAt(0)]))
                    );
                    Element e4 = pairing.getFieldAt(depth).newElement().powZn(
                            rs[index].sub(b.mul(rs[gate.getInputIndexAt(1)]))
                    );

                    keys.put(index, new Element[]{e1, e2, e3, e4});
                    break;

                case AND:
                    a = pairing.getZr().newRandomElement();
                    b = pairing.getZr().newRandomElement();

                    e1 = pairing.getG1().newElement().powZn(a);
                    e2 = pairing.getG1().newElement().powZn(b);

                    e3 = pairing.getFieldAt(depth).newElement().powZn(
                            rs[index].sub(a.mul(rs[gate.getInputIndexAt(0)]))
                                    .sub(b.mul(rs[gate.getInputIndexAt(1)]))
                    );

                    keys.put(index, new Element[]{e1, e2, e3});
                    break;
            }
        }

        return new GGHSW13SecretKeyParameters(
                param.getPublicKeyParameters().getParameters(), circuit, keys
        );
    }

}