package org.arcanum.field.poly.fft;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class FFTTest {

    @Test
    public void testAddSub() {
        FFTPrimeGenerator fftPrimeGenerator = new FFTPrimeGenerator();
        FFTPrime fftPrime = fftPrimeGenerator.next();

        int k = 1;
        int l = 1 << k;

        long[] A = new long[l];
        long[] B = new long[l];

        long[] a = new long[l];
        Random random = new Random();

        for (int i = 0; i < l; i++)
            a[i] = random.nextInt((int) fftPrime.getQ());

        System.out.println(Arrays.toString(a));

        FFT.fft(A, a, k, fftPrime.getQ(), fftPrime.getRootTable());

        System.out.println(Arrays.toString(A));
        FFT.fft(B, A, k, fftPrime.getQ(), fftPrime.getRootInvTable());

        System.out.println(Arrays.toString(B));
    }

}