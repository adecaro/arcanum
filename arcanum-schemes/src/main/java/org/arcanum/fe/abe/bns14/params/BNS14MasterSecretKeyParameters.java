package org.arcanum.fe.abe.bns14.params;


import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.fe.params.KeyParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14MasterSecretKeyParameters extends KeyParameters<BNS14Parameters> {

    private ElementCipherParameters latticeSk;


    public BNS14MasterSecretKeyParameters(BNS14Parameters parameters, ElementCipherParameters latticeSk) {
        super(true, parameters);

        this.latticeSk = latticeSk;
    }

    public ElementCipherParameters getLatticeSk() {
        return latticeSk;
    }
}