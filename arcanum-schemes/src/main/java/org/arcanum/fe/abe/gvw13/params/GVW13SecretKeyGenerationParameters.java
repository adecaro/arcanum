package org.arcanum.fe.abe.gvw13.params;

import org.arcanum.circuit.BooleanCircuit;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13SecretKeyGenerationParameters extends KeyGenerationParameters {

    private GVW13PublicKeyParameters publicKeyParameters;
    private GVW13MasterSecretKeyParameters masterSecretKeyParameters;
    private BooleanCircuit circuit;


    public GVW13SecretKeyGenerationParameters(
            GVW13PublicKeyParameters publicKeyParameters,
            GVW13MasterSecretKeyParameters masterSecretKeyParameters,
            BooleanCircuit circuit) {
        super(null, 0);

        this.publicKeyParameters = publicKeyParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
        this.circuit = circuit;
    }


    public GVW13PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public GVW13MasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }

    public BooleanCircuit getCircuit() {
        return circuit;
    }
}