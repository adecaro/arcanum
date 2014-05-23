package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.Sampler;
import org.arcanum.field.floating.ApfloatUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ZGaussianProber {

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        int iterations = 1000000;
//        int iterations = 10;

        Apfloat sigma = ApfloatUtils.newApfloat(10);

        Sampler<BigInteger> sampler = new DiscreteGaussianRSSampler(
                random,
                sigma
        );


        Map<BigInteger, Integer> values = new HashMap<BigInteger, Integer>();
        for (int i = 0; i < iterations; i++) {

            if (i % 10000 == 0)
                System.out.print(".");
            if (i % 100000 == 0)
                System.out.println();

            BigInteger value = sampler.sample();

            if (!values.containsKey(value)) {
                values.put(value, 1);
            } else
                values.put(value, values.get(value) + 1);
        }
        System.out.println();

        BigInteger[] keys = values.keySet().toArray(new BigInteger[values.size()]);
        Arrays.sort(keys);

        for (BigInteger key : keys) {
            System.out.println(key + " " + (values.get(key)/(double)iterations));
        }
    }


}
