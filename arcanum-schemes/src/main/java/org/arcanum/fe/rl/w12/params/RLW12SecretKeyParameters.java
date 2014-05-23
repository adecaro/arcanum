package org.arcanum.fe.rl.w12.params;

import org.arcanum.Element;
import org.arcanum.dfa.DFA;
import org.arcanum.field.util.ElementUtils;

import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class RLW12SecretKeyParameters extends RLW12KeyParameters {
    private DFA dfa;

    private Element[] kStarts;
    private Map<DFA.Transition, Element[]> kTransitions;
    private Map<Integer, Element[]> kEnds;

    public RLW12SecretKeyParameters(RLW12Parameters parameters,
                                    DFA dfa,
                                    Element[] kStarts,
                                    Map<DFA.Transition, Element[]> kTransitions,
                                    Map<Integer, Element[]> kEnds) {
        super(true, parameters);

        this.dfa = dfa;
        this.kStarts = ElementUtils.cloneImmutable(kStarts);
        this.kTransitions = ElementUtils.cloneImmutable(kTransitions);
        this.kEnds = ElementUtils.cloneImmutable(kEnds);
    }

    public DFA getDfa() {
        return dfa;
    }

    public Element getkStart(int index) {
        return kStarts[index];
    }

    public Element getkTransition(DFA.Transition transition, int index) {
        return kTransitions.get(transition)[index];
    }

    public Element getkEnd(int state, int index) {
        return kEnds.get(state)[index];
    }
}