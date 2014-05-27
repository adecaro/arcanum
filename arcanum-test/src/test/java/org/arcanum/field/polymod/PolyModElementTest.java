package org.arcanum.field.polymod;

import junit.framework.TestCase;
import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.Parameters;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.pairing.d.TypeDPairing;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PolyModElementTest extends TestCase {

    protected Pairing pairing;

    @Override
    protected void setUp() throws Exception {
        pairing = new TypeDPairing(getParameters());

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    protected Parameters getParameters() {
        return PairingFactory.getInstance().loadParameters("it/unisa/dia/gas/plaf/arcanum/pairing/d/d_9563.properties");
    }


    public void testBytesMethod() {
        Element source = pairing.getGT().newRandomElement();
        byte[] buffer = source.toBytes();
        Element target = pairing.getGT().newElement();
        int len = target.setFromBytes(buffer);

        assertEquals(true, source.isEqual(target));
        assertEquals(buffer.length, len);
    }

}
