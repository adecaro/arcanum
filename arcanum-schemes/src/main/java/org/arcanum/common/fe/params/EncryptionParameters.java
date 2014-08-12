package org.arcanum.common.fe.params;

import org.arcanum.program.Assignment;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class EncryptionParameters<MPK extends CipherParameters, A> implements CipherParameters {

    private MPK mpk;
    private Assignment<A> assignment;


    public EncryptionParameters(MPK mpk, Assignment<A> assignment) {
        this.mpk = mpk;
        this.assignment = assignment;
    }


    public MPK getMpk() {
        return mpk;
    }

    public Assignment<A> getAssignment() {
        return assignment;
    }
}
