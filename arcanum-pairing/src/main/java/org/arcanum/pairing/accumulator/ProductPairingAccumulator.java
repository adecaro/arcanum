package org.arcanum.pairing.accumulator;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.PairingPreProcessing;
import org.arcanum.common.concurrent.Pool;
import org.arcanum.common.concurrent.accumultor.Accumulator;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class ProductPairingAccumulator implements PairingAccumulator {

    private Pairing pairing;

    private int cursor;
    private Element[] in1, in2;
    private Element result;


    public ProductPairingAccumulator(Pairing pairing, int n) {
        this.pairing = pairing;
        this.in1 = new Element[n];
        this.in2 = new Element[n];
        this.cursor = 0;
    }


    public Accumulator<Element> accumulate(Callable<Element> callable) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Accumulator<Element> awaitTermination() {
        awaitResult();

        return this;
    }

    public Element getResult() {
        return result;
    }


    public Future<Element> submitFuture(Callable<Element> callable) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Pool<Element> submit(Callable<Element> callable) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Pool<Element> submit(Runnable runnable) {
        throw new IllegalStateException("Not supported!!!");
    }



    public PairingAccumulator addPairing(Element e1, Element e2) {
        in1[cursor] =  e1;
        in2[cursor++] =  e2;

        return this;
    }

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public PairingAccumulator addPairingInverse(Element e1, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Element awaitResult(){
        return (result = pairing.pairing(in1, in2));
    }

}
