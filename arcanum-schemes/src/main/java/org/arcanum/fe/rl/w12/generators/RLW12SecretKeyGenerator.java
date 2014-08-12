package org.arcanum.fe.rl.w12.generators;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.common.fe.generator.SecretKeyGenerator;
import org.arcanum.fe.rl.w12.params.RLW12MasterSecretKeyParameters;
import org.arcanum.fe.rl.w12.params.RLW12PublicKeyParameters;
import org.arcanum.fe.rl.w12.params.RLW12SecretKeyParameters;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.program.dfa.DFA;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class RLW12SecretKeyGenerator extends SecretKeyGenerator<RLW12PublicKeyParameters, RLW12MasterSecretKeyParameters, DFA> {

    private Pairing pairing;

    @Override
    public SecretKeyGenerator<RLW12PublicKeyParameters, RLW12MasterSecretKeyParameters, DFA> init(AsymmetricCipherKeyPair keyPair) {
        super.init(keyPair);

        this.pairing = PairingFactory.getPairing(secretKey.getParameters().getParameters());

        return this;
    }

    public CipherParameters generateKey(DFA dfa) {
        int ns = dfa.getNumStates();

        Element[] kStarts = new Element[2];
        Map<DFA.Transition, Element[]> kTransitions = new HashMap<DFA.Transition, Element[]>();
        Map<Integer, Element[]> kEnds = new HashMap<Integer, Element[]>();

        Element[] Ds = new Element[ns];
        for (int i = 0; i < ns; i++) {
            Ds[i] = pairing.getG1().newRandomElement().getImmutable();
        }

        // Initial state
        Element rs = pairing.getZr().newRandomElement();
        kStarts[0] = Ds[0].mul(publicKey.gethStart().powZn(rs));
        kStarts[1] = publicKey.getParameters().getG().powZn(rs);

        // Transitions
        for (int i = 0, size = dfa.getNumTransitions(); i < size; i++) {
            DFA.Transition transition = dfa.getTransitionAt(i);

            Element rt = pairing.getZr().newRandomElement();

            kTransitions.put(
                    transition,
                    new Element[]{
                            Ds[transition.getFrom()].invert().mul(publicKey.getZ().powZn(rt)),
                            publicKey.getParameters().getG().powZn(rt),
                            Ds[transition.getTo()].mul(publicKey.getHAt(transition.getReading()).powZn(rt))
                    }
            );
        }

        // Final states
        Element secret = publicKey.getParameters().getG().powZn(secretKey.getAlpha().negate()).getImmutable();
        for (int i = 0, size = dfa.getNumFinalStates(); i < size; i++) {
            int finalState = dfa.getFinalStateAt(i);

            Element rf = pairing.getZr().newRandomElement();

            kEnds.put(
                    finalState,
                    new Element[]{
                        secret.mul(Ds[finalState]).mul(publicKey.gethEnd().powZn(rf)),
                        publicKey.getParameters().getG().powZn(rf)
                    }
            );
        }

        return new RLW12SecretKeyParameters(
                publicKey.getParameters(),
                dfa, kStarts, kTransitions, kEnds
        );
    }

}