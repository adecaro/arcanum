package org.arcanum.pairing.accumulator;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.PairingPreProcessing;
import org.arcanum.util.concurrent.accumultor.AbstractAccumulator;
import org.arcanum.util.concurrent.accumultor.Accumulator;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public abstract class AbstractPairingAccumulator extends AbstractAccumulator<Element> implements PairingAccumulator {

    protected Pairing pairing;


    public AbstractPairingAccumulator(Pairing pairing) {
        this(pairing, pairing.getGT().newOneElement());
    }

    public AbstractPairingAccumulator(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.result = value;
    }


    public Accumulator<Element> accumulate(Callable<Element> callable) {
        throw new IllegalStateException("Invalid call method!");
    }

    public PairingAccumulator addPairing(final Element e1, final Element e2) {
        super.accumulate(new Callable<Element>() {
            public Element call() throws Exception {
                return pairing.pairing(e1, e2);
            }
        });

        return this;
    }

    public PairingAccumulator addPairingInverse(final Element e1, final Element e2) {
        super.accumulate(new Callable<Element>() {
            public Element call() throws Exception {
                return pairing.pairing(e1, e2).invert();
            }
        });

        return this;
    }

    public PairingAccumulator addPairing(final PairingPreProcessing pairingPreProcessing, final Element e2) {
        super.accumulate(new Callable<Element>() {
            public Element call() throws Exception {
                return pairingPreProcessing.pairing(e2);
            }
        });

        return this;
    }

}
