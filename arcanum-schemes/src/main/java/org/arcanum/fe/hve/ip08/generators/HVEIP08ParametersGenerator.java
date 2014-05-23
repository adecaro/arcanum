package org.arcanum.fe.hve.ip08.generators;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.Parameters;
import org.arcanum.fe.hve.ip08.params.HVEIP08Parameters;
import org.arcanum.pairing.PairingFactory;

import java.util.Arrays;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class HVEIP08ParametersGenerator {
    private Parameters curveParams;
    private int[] attributeLengths;

    private Pairing pairing;


    public void init(Parameters curveParams, int... attributeLengths) {
        this.curveParams = curveParams;
        this.attributeLengths = Arrays.copyOf(attributeLengths, attributeLengths.length);

        this.pairing = PairingFactory.getPairing(curveParams);
    }

    public void init(int n, Parameters curveParams) {
        this.init(n, 1, curveParams);
    }

    public void init(int n, int numBitsPerAttribute, Parameters curveParams) {
        this.curveParams = curveParams;
        this.attributeLengths = new int[n];
        for (int i = 0; i < attributeLengths.length; i++) {
            attributeLengths[i] = numBitsPerAttribute;
        }

        this.pairing = PairingFactory.getPairing(curveParams);
    }


    public HVEIP08Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new HVEIP08Parameters(curveParams, g.getImmutable(), attributeLengths);
    }

}