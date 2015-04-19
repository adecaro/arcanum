package org.arcanum.field.poly.fft;

import org.arcanum.Element;
import org.arcanum.field.z.SinglePrecisionModularArithmetic;
import org.arcanum.field.z.ZrField;

import java.math.BigInteger;
import java.util.Random;

import static org.arcanum.field.poly.fft.FFTUtils.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class FFTPrimeGenerator {

    public static long isFFTPrime(long p) {
        long m, x, y, z;
        long j, k;


        if (p <= 1 || p >= NTL_SP_BOUND) return -1;
        if (p % 2 == 0) return -1;
        if (p % 3 == 0) return -1;
        if (p % 5 == 0) return -1;
        if (p % 7 == 0) return -1;

        m = p - 1;
        k = 0;
        while ((m & 1) == 0) {
            m = m >> 1;
            k++;
        }

        Random random = new Random();
        for (; ; ) {
            x = random.nextLong();
            if (x >= p) continue;


            if (x == 0) continue;
            z = SinglePrecisionModularArithmetic.PowerMod(x, m, p);
            if (z == 1) continue;

            x = z;
            j = 0;
            do {
                y = z;
                z = SinglePrecisionModularArithmetic.MulMod(y, y, p);
                j++;
            } while (j != k && z != 1);

            if (z != 1 || y != p - 1)
                return -1;

            if (j == k)
                break;
        }

        /* x^{2^k} = 1 mod p, x^{2^{k-1}} = -1 mod p */

        long TrialBound = m >> k;
        if (TrialBound > 0) {
            if (!BigInteger.valueOf(p).isProbablePrime(5)) return -1;

            /* we have to do trial division by special numbers */
            TrialBound = SinglePrecisionModularArithmetic.SqrRoot(TrialBound);
            for (long a = 1, b; a <= TrialBound; a++) {
                b = (a << k) + 1;
                if (p % b == 0) return -1;
            }
        }

        /* p is an FFT prime */
        for (j = FFTUtils.NTL_FFTMaxRoot; j < k; j++)
            x = SinglePrecisionModularArithmetic.MulMod(x, x, p);

        return x;
    }

    private long m, k;
    private long q, w;


    public FFTPrimeGenerator() {
        this.m = NTL_FFTMaxRootBnd + 1;
        this.k = 0;
    }

    public FFTPrime next() {
        long t, cand;

        for (; ; ) {
            if (k == 0) {
                m--;
                if (m < 5)
                    throw new IllegalStateException("ran out of FFT primes");
                k = 1L << (NTL_SP_NBITS - m - 2);
            }

            k--;

            cand = (1L << (NTL_SP_NBITS - 1)) + (k << (m + 1)) + (1L << m) + 1;

            t = isFFTPrime(cand);

            if (t < 0)
                continue;
            q = cand;
            w = t;

            return new FFTPrime(q, w, false);
        }
    }


    public long getQ() {
        return q;
    }

    public long getW() {
        return w;
    }


    public static void main(String[] args) {
        FFTPrimeGenerator fftPrimeGenerator = new FFTPrimeGenerator();

        while (true) {
            fftPrimeGenerator.next();

            System.out.println(fftPrimeGenerator.getQ());
            System.out.println(fftPrimeGenerator.getW());
            System.out.println("-----------------------");

            ZrField field = new ZrField(fftPrimeGenerator.getQ());
            Element w = field.newElement(fftPrimeGenerator.getW());


            System.out.println(field.newRandomElement().pow(BigInteger.valueOf(fftPrimeGenerator.getQ() - 1)));

            long n = FFTUtils.calcMaxRoot(fftPrimeGenerator.getQ());

            System.out.println("n = " + n);
            Element test = w.duplicate().pow(BigInteger.valueOf(1L << n));

            System.out.println("test = " + test);

//            if (test.toBigInteger().longValue() != 1)
                break;

        }
    }

}
