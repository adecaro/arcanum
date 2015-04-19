package org.arcanum.field.poly.fft;

import java.math.BigInteger;
import java.util.Vector;

import static org.arcanum.field.poly.fft.FFTUtils.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class FFTInfo {

    private int numPrimes;
    private int maxRoot;
    private boolean QuickCRT;
    private BigInteger MinusMModP;  //  -M mod p, M = product of primes


    // the following arrays are indexed 0..numPrimes-1
    // q = FFTPrime[i]
    Vector<Double> x;          // u/q, where u = (M/q)^{-1} mod q
    Vector<Long> u;            // u, as above


    public FFTInfo(BigInteger order) {
        System.out.println("order = " + order);
        BigInteger B, M1, M2, M3;
        long t;

        B = order.multiply(order).shiftLeft((int) (FFTUtils.NTL_FFTMaxRoot + FFTUtils.NTL_FFTFudge));


        // FIXME: the following is quadratic time...would
        // be nice to get a faster solution...
        // One could estimate the # of primes by summing logs,
        // then multiply using a tree-based multiply, then
        // adjust up or down...

        // Assuming IEEE floating point, the worst case estimate
        // for error guarantees a correct answer +/- 1 for
        // numprimes up to 2^25...for sure we won't be
        // using that many primes...we can certainly put in
        // a sanity check, though.

        // If I want a more accuaruate summation (with using Kahan,
        // which has some portability issues), I could represent
        // numbers as x = a + f, where a is integer and f is the fractional
        // part.  Summing in this representation introduces an *absolute*
        // error of 2 epsilon n, which is just as good as Kahan
        // for this application.

        // same strategy could also be used in the ZZX HomMul routine,
        // if we ever want to make that subquadratic

        BigInteger M = BigInteger.ONE;
        int n = 0;
        long q = 0;
        FFTTable fftTable = FFTTable.getInstance();
        while (M.compareTo(B) <= 0) {
            q = fftTable.getAt(n++).getQ();
            M = M.multiply(BigInteger.valueOf(q));
        }

        this.numPrimes = n;
        this.maxRoot = (int) calcMaxRoot(q);

        double fn = (double) n;

        if (8.0 * fn * (fn + 32) > NTL_FDOUBLE_PRECISION)
            throw new IllegalArgumentException("modulus too big");

        this.QuickCRT = 8.0 * fn * (fn + 32) <= NTL_FDOUBLE_PRECISION / (double) NTL_SP_BOUND;

        this.x = new Vector<>(n);
        this.u = new Vector<>(n);

        /*
        FFTInfo->rem_struct.init(n, ZZ_pInfo->order, GetFFTPrime);
        FFTInfo->crt_struct.init(n, ZZ_pInfo->order, GetFFTPrime);

        if (!FFTInfo->crt_struct.special()) {
            ZZ qq, rr;

            DivRem(qq, rr, M, ZZ_pInfo->order);

            NegateMod(FFTInfo->MinusMModP, rr, ZZ_pInfo->order);

            for (i = 0; i < n; i++) {
                q = GetFFTPrime(i);

                long tt = rem(qq, q);

                mul(M2, ZZ_pInfo->order, tt);
                add(M2, M2, rr);
                div(M2, M2, q);  // = (M/q) rem order


                div(M1, M, q);
                t = rem(M1, q);
                t = InvMod(t, q);

                mul(M3, M2, t);
                rem(M3, M3, ZZ_pInfo->order);

                FFTInfo->crt_struct.insert(i, M3);


                FFTInfo->x[i] = ((double) t)/((double) q);
                FFTInfo->u[i] = t;
            }
        }

        tmps = MakeSmart<ZZ_pTmpSpaceT>();
        tmps->crt_tmp_vec.fetch(FFTInfo->crt_struct);
        tmps->rem_tmp_vec.fetch(FFTInfo->rem_struct);

        */
    }


    public int getNumPrimes() {
        return numPrimes;
    }

    public int getMaxRoot() {
        return maxRoot;
    }

    public boolean isQuickCRT() {
        return QuickCRT;
    }


}
