package org.arcanum.field.poly.fft;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class FFTUtils {
    public static long NTL_BITS_PER_LONG= 64;
    public static long NTL_MAX_LONG= 9223372036854775807L;
    public static long NTL_MAX_INT= 2147483647;
    public static long NTL_BITS_PER_INT= 32;
    public static long NTL_BITS_PER_SIZE_T= 64;
    public static long NTL_ARITH_RIGHT_SHIFT= 1;
    public static long NTL_NBITS_MAX= 50;
    public static long  NTL_DOUBLE_PRECISION= 53;
    public static double NTL_FDOUBLE_PRECISION= ((double)(1L<<52));
    public static double NTL_QUAD_FLOAT_SPLIT= (((double)(1L<<27)))+1.0;
    public static long NTL_EXT_DOUBLE= 0;


    public static long NTL_NBITS = NTL_NBITS_MAX;
    public static long NTL_SP_NBITS = NTL_NBITS;
    // Absolute maximum root bound for FFT primes.
    // Don't change this!
    public static long NTL_FFTMaxRootBnd = NTL_SP_NBITS - 2;
    // Root bound for FFT primes.  Held to a maximum
    // of 25 to avoid large tables and excess precomputation,
    // and to keep the number of FFT primes needed small.
    // This means we can multiply polynomials of degree less than 2^24.
    // This can be increased, with a slight performance penalty.
    public static long NTL_FFTMaxRoot = (25 <= NTL_FFTMaxRootBnd) ? 25 : NTL_FFTMaxRootBnd;
    public static long NTL_SP_BOUND = (1L << NTL_SP_NBITS);

    public static double NTL_SP_FBOUND = ((double) NTL_SP_BOUND);


    public static long NTL_FFTFudge = 4;
// This constant is used in selecting the correct
// number of FFT primes for polynomial multiplication
// in ZZ_pX and zz_pX.  Set at 4, this allows for
// two FFT reps to be added or subtracted once,
// before performing CRT, and leaves a reasonable margin for error.
// Don't change this!


    // Must have NTL_BRC_THRESH > 2*NTL_BRC_Q
// Should also have (1L << (2*NTL_BRC_Q)) small enough
// so that we can fit that many long's into the cache
    public static long NTL_BRC_THRESH = 12;
    public static long  NTL_BRC_Q = 5;

    public static int  NTL_MAX_FFTPRIMES = 20000;
// for a thread-safe implementation, it is most convenient to
// impose a reasonabel upper bound on he number of FFT primes.
// without this restriction, a growing table would have to be
// relocated in one thread, leaving dangling pointers in
// another thread.  Each entry in the table is just a poiner,
// so this does not incur too much space overhead.
// One could alo implement a 2D-table, which would allocate
// rows on demand, thus reducing wasted space at the price
// of extra arithmetic to actually index into the table.
// This may be an option to consider at some point.

// At the current setting of 20000, on 64-bit machines with 50-bit
// FFT primes, this allows for polynomials with 20*50/2 = 500K-bit
// coefficients, while the table itself takes 160KB.



    public  static long calcMaxRoot(long p) {
        p = p - 1;

        long k = 0;
        while ((p & 1) == 0) {
            p = p >> 1;
            k++;
        }

        System.out.println("calcMaxRoot.k = " + k);

        if (k > NTL_FFTMaxRoot)
            return NTL_FFTMaxRoot;
        else
            return k;
    }
}
