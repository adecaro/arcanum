package org.arcanum.fe.abe.gghvv13.params;

import org.arcanum.circuit.BooleanCircuit;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHVV13SecretKeyGenerationParameters extends KeyGenerationParameters {

    private GGHVV13PublicKeyParameters publicKeyParameters;
    private GGHVV13MasterSecretKeyParameters masterSecretKeyParameters;
    private BooleanCircuit circuit;


    public GGHVV13SecretKeyGenerationParameters(
            GGHVV13PublicKeyParameters publicKeyParameters,
            GGHVV13MasterSecretKeyParameters masterSecretKeyParameters,
            BooleanCircuit circuit) {
        super(null, 0);

        this.publicKeyParameters = publicKeyParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
        this.circuit = circuit;
    }


    public GGHVV13PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public GGHVV13MasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }

    public BooleanCircuit getCircuit() {
        return circuit;
    }
}