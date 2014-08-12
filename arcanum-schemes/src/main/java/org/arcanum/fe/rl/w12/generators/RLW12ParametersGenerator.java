package org.arcanum.fe.rl.w12.generators;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.Parameters;
import org.arcanum.fe.rl.w12.params.RLW12Parameters;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.program.dfa.DFA;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class RLW12ParametersGenerator {
    private Parameters parameters;
    private DFA.Alphabet alphabet;

    private Pairing pairing;


    public RLW12ParametersGenerator init(Parameters parameters, DFA.Alphabet alphabet) {
        this.parameters = parameters;
        this.alphabet = alphabet;
        this.pairing = PairingFactory.getPairing(this.parameters);

        return this;
    }


    public RLW12Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new RLW12Parameters(parameters, g.getImmutable(), alphabet);
    }

}