package org.arcanum.field.poly.fft;

import java.util.List;
import java.util.Vector;

import static org.arcanum.field.z.SinglePrecisionModularArithmetic.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class FFT {

    public static boolean NTL_LOOP_UNROLL = true;


    /**
     * performs a 2^k-point convolution modulo q
     */
    public static void fft(long[] A, long[] a, long k, long q, long[] root) {
        if (k <= 1) {
            if (k == 0) {
                A[0] = a[0];
                return;
            }
            if (k == 1) {
                long a0 = AddMod(a[0], a[1], q);
                long a1 = SubMod(a[0], a[1], q);
                A[0] = a0;
                A[1] = a1;
                return;
            }
        }

        // assume k > 1

        long[] wtab = new long[1 << (k-2)];
        double[] wqinvtab = new double[1 << (k-2)];

        double qinv = 1/((double) q); // TODO: Already computed in FFTPrime

        wtab[0] = 1;
        wqinvtab[0] = PrepMulModPrecon(1, q, qinv);

        long[] AA = BitReverseCopy(a, k);

        long n = 1L << k;

        int s, m, m_half, m_fourth, i, j;
        long t, u, t1, u1, tt, tt1;

        long w;
        double wqinv;

        // s = 1

        for (i = 0; i < n; i += 2) {
            t = AA[i + 1];
            u = AA[i];
            AA[i] = AddMod(u, t, q);
            AA[i+1] = SubMod(u, t, q);
        }



        for (s = 2; s < k; s++) {
            m = 1 << s;
            m_half = 1 << (s-1);
            m_fourth = 1 << (s-2);

            w = root[s];
            wqinv = PrepMulModPrecon(w, q, qinv);

            // prepare wtab...

            if (s == 2) {
                wtab[1] = MulModPrecon(wtab[0], w, q, wqinv);
                wqinvtab[1] = PrepMulModPrecon(wtab[1], q, qinv);
            }
            else {
                // some software pipelining

                i = m_half-1; j = m_fourth-1;
                wtab[i-1] = wtab[j];
                wqinvtab[i-1] = wqinvtab[j];
                wtab[i] = MulModPrecon(wtab[i-1], w, q, wqinv);

                i -= 2; j --;

                for (; i >= 0; i -= 2, j --) {
                    long wp2 = wtab[i+2];
                    long wm1 = wtab[j];
                    wqinvtab[i+2] = PrepMulModPrecon(wp2, q, qinv);
                    wtab[i-1] = wm1;
                    wqinvtab[i-1] = wqinvtab[j];
                    wtab[i] = MulModPrecon(wm1, w, q, wqinv);
                }

                wqinvtab[1] = PrepMulModPrecon(wtab[1], q, qinv);
            }

            for (i = 0; i < n; i+= m) {
                int AA0 = i;
                int AA1 = i + m_half;



                if (NTL_LOOP_UNROLL) {
                    t = AA[AA1+0];
                    u = AA[AA0+0];
                    t1 = MulModPrecon(AA[AA1+1], w, q, wqinv);
                    u1 = AA[AA0+1];


                    for (j = 0; j < m_half - 2; j += 2) {
                        long a02 = AA[AA0+j + 2];
                        long a03 = AA[AA0+j + 3];
                        long a12 = AA[AA1+j + 2];
                        long a13 = AA[AA1+j + 3];
                        long w2 = wtab[j + 2];
                        long w3 = wtab[j + 3];
                        double wqi2 = wqinvtab[j + 2];
                        double wqi3 = wqinvtab[j + 3];

                        tt = MulModPrecon(a12, w2, q, wqi2);
                        long b00 = AddMod(u, t, q);
                        long b10 = SubMod(u, t, q);
                        t = tt;
                        u = a02;

                        tt1 = MulModPrecon(a13, w3, q, wqi3);
                        long b01 = AddMod(u1, t1, q);
                        long b11 = SubMod(u1, t1, q);
                        t1 = tt1;
                        u1 = a03;

                        AA[AA0+j] = b00;
                        AA[AA1+j] = b10;
                        AA[AA0+j + 1] = b01;
                        AA[AA1+j + 1] = b11;
                    }


                    AA[AA0+j] = AddMod(u, t, q);
                    AA[AA1+j] = SubMod(u, t, q);
                    AA[AA0+j + 1] = AddMod(u1, t1, q);
                    AA[AA1+j + 1] = SubMod(u1, t1, q);
                } else {


                    t = AA[AA1+0];
                    u = AA[AA0+0];

                    for (j = 0; j < m_half - 1; j++) {
                        long a02 = AA[AA0+j + 1];
                        long a12 = AA[AA1+j + 1];
                        long w2 = wtab[j + 1];
                        double wqi2 = wqinvtab[j + 1];

                        tt = MulModPrecon(a12, w2, q, wqi2);
                        long b00 = AddMod(u, t, q);
                        long b10 = SubMod(u, t, q);
                        t = tt;
                        u = a02;

                        AA[AA0+j] = b00;
                        AA[AA1+j] = b10;
                    }


                    AA[AA0+j] = AddMod(u, t, q);
                    AA[AA1+j] = SubMod(u, t, q);
                }
            }
        }


        // s == k...special case

        m = 1 << s;
        m_half = 1 << (s-1);
        m_fourth = 1 << (s-2);

        w = root[s];
        wqinv = PrepMulModPrecon(w, q, qinv);

        // j = 0, 1

        t = AA[m_half];
        u = AA[0];
        t1 = MulModPrecon(AA[1+ m_half], w, q, wqinv);
        u1 = AA[1];

        A[0] = AddMod(u, t, q);
        A[m_half] = SubMod(u, t, q);
        A[1] = AddMod(u1, t1, q);
        A[1 + m_half] = SubMod(u1, t1, q);

        for (j = 2; j < m_half; j += 2) {
            t = MulModPrecon(AA[j + m_half], wtab[j >> 1], q, wqinvtab[j >> 1]);
            u = AA[j];
            t1 = MulModPrecon(AA[j + 1+ m_half], wtab[j >> 1], q,
                    wqinvtab[j >> 1]);
            t1 = MulModPrecon(t1, w, q, wqinv);
            u1 = AA[j + 1];

            A[j] = AddMod(u, t, q);
            A[j + m_half] = SubMod(u, t, q);
            A[j + 1] = AddMod(u1, t1, q);
            A[j + 1 + m_half] = SubMod(u1, t1, q);

        }
    }


    static long[] BitReverseCopy(long[]  A, long k) {
        return BasicBitReverseCopy(A, k);
    }

    static long[] BasicBitReverseCopy(long[] A, long k){
        long[] B = new long[1 << k];

        long n = 1L << k;

        if (brc_mem[(int) k] == null)
            BRC_init(k);

        for (int i = 0; i < n; i++)
            B[brc_mem[(int) k].get(i)] = A[i];

        return B;
    }


    static Vector<Integer>[] brc_mem = new Vector[(int) FFTUtils.NTL_FFTMaxRoot + 1];

    static List<Integer> BRC_init(long k)
    {
        int n = (1 << k);
        brc_mem[(int) k] = new Vector<>(n);
        brc_mem[(int) k].setSize(n*n);

        int i, j;
        for (i = 0, j = 0; i < n; i++, j = RevInc(j, k))
            brc_mem[(int) k].set(i, j);

        return brc_mem[(int) k];
    }

    static
    int RevInc(long a, long k)
    {
        int j, m;

        j = (int) k;
        m = 1 << (k-1);

        while (j != 0 && ((m & a) != 0)) {
            a ^= m;
            m >>= 1;
            j--;
        }
        if (j != 0)
            a ^= m;

        return (int) a;
    }

}
