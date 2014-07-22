package org.arcanum.util.concurrent;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ThreadSecureRandom {

    private static final ThreadLocal<SecureRandom> secureRandom = new ThreadLocal<SecureRandom>() {
        @Override
        protected SecureRandom initialValue() {
            try {
                return SecureRandom.getInstance("SHA1PRNG");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return new SecureRandom();
            }
        }
    };

    public static SecureRandom get() {
        return secureRandom.get();
    }

}
