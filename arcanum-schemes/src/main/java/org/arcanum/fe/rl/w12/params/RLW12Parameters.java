package org.arcanum.fe.rl.w12.params;

import org.arcanum.Element;
import org.arcanum.Parameters;
import org.arcanum.program.dfa.DFA;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class RLW12Parameters implements CipherParameters {
    private Parameters parameters;
    private Element g;
    private DFA.Alphabet alphabet;


    public RLW12Parameters(Parameters parameters, Element g, DFA.Alphabet alphabet) {
        this.parameters = parameters;
        this.g = g.getImmutable();
        this.alphabet = alphabet;
    }


    public Parameters getParameters() {
        return parameters;
    }

    public Element getG() {
        return g;
    }

    public int getAlphabetSize() {
        return alphabet.getSize();
    }

    public int getCharacterIndex(Character character) {
        return alphabet.getIndex(character);
    }

}