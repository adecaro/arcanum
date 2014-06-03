package org.arcanum.pairing;

import junit.framework.TestCase;
import org.arcanum.Pairing;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PairingFactoryTest extends TestCase {

    public void testReusing() {
        PairingFactory.getInstance().setReuseInstance(true);
        PairingFactory.getInstance().setUsePBCWhenPossible(false);

        Pairing pairing1 = PairingFactory.getPairing("org/arcanum/pairing/a/a_181_603.properties");
        Pairing pairing2 = PairingFactory.getPairing("org/arcanum/pairing/a/a_181_603.properties");

        assertEquals(true, pairing1 == pairing2);
    }

    public void testAvoidReusing() {
        PairingFactory.getInstance().setReuseInstance(false);
        PairingFactory.getInstance().setUsePBCWhenPossible(false);

        Pairing pairing1 = PairingFactory.getPairing("org/arcanum/pairing/a/a_181_603.properties");
        Pairing pairing2 = PairingFactory.getPairing("org/arcanum/pairing/a/a_181_603.properties");

        assertEquals(true, pairing1 != pairing2);
    }
}
