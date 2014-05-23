package org.arcanum.fe.abe.gghsw13.params;

import org.arcanum.circuit.BooleanCircuit;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13SecretKeyGenerationParameters extends KeyGenerationParameters {

    private GGHSW13PublicKeyParameters publicKeyParameters;
    private GGHSW13MasterSecretKeyParameters masterSecretKeyParameters;
    private BooleanCircuit circuit;


    public GGHSW13SecretKeyGenerationParameters(
            GGHSW13PublicKeyParameters publicKeyParameters,
            GGHSW13MasterSecretKeyParameters masterSecretKeyParameters,
            BooleanCircuit circuit) {
        super(null, 0);

        this.publicKeyParameters = publicKeyParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
        this.circuit = circuit;
    }


    public GGHSW13PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public GGHSW13MasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }

    public BooleanCircuit getCircuit() {
        return circuit;
    }
}