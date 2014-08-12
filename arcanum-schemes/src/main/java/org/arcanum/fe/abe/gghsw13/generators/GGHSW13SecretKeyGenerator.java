package org.arcanum.fe.abe.gghsw13.generators;

import org.arcanum.Element;
import org.arcanum.common.fe.generator.SecretKeyGenerator;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13MasterSecretKeyParameters;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13PublicKeyParameters;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13SecretKeyParameters;
import org.arcanum.pairing.Pairing;
import org.arcanum.program.circuit.BooleanCircuit;
import org.arcanum.program.circuit.BooleanGate;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13SecretKeyGenerator extends SecretKeyGenerator<GGHSW13PublicKeyParameters, GGHSW13MasterSecretKeyParameters, BooleanCircuit>  {
    private Pairing pairing;

    @Override
    public SecretKeyGenerator<GGHSW13PublicKeyParameters, GGHSW13MasterSecretKeyParameters, BooleanCircuit> init(AsymmetricCipherKeyPair keyPair) {
        super.init(keyPair);

        this.pairing = secretKey.getParameters().getPairing();

        return this;
    }

    public CipherParameters generateKey(BooleanCircuit circuit) {
        // sample the randomness
        Element[] rs = new Element[circuit.getNumInputs() + circuit.getNumGates()];
        for (int i = 0; i < rs.length; i++)
            rs[i] = pairing.getZr().newRandomElement().getImmutable();

        // encode the circuit

        Map<Integer, Element[]> keys = new HashMap<Integer, Element[]>();

        Element ePrime = pairing.getFieldAt(circuit.getDepth()).newElement().powZn(secretKey.getAlpha().sub(rs[rs.length - 1]));
        keys.put(-1, new Element[]{ePrime});

        for (BooleanGate gate : circuit) {
            int index = gate.getIndex();
            int depth = gate.getDepth();

            switch (gate.getType()) {
                case INPUT:

                    Element z = pairing.getZr().newRandomElement();

                    Element e1 = pairing.getG1().newElement().powZn(rs[index]).mul(publicKey.getHAt(index).powZn(z));
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

        return new GGHSW13SecretKeyParameters(publicKey.getParameters(), circuit, keys);
    }

}