package org.arcanum.fhe.gsw14.field;

import junit.framework.TestCase;
import org.arcanum.Element;
import org.arcanum.permutation.DefaultPermutation;
import org.arcanum.permutation.Permutation;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

public class AP14HESymGroupFieldTest extends TestCase {

    private SecureRandom random;
    private AP14HESymGroupField field;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
        field = new AP14HESymGroupField(new GSW14Field(random, 4, 30), 5);
    }

    @Test
    public void testNewElement() {
        Permutation perm = new DefaultPermutation(4, 3, 2, 1, 0);

        Element a = field.newElement(perm);

        Element result = ((AP14HESymGroupElement)a).apply(perm);

        assertEquals(1, result.toBigInteger());
    }
}