package org.arcanum.field.poly.fft;

import static org.arcanum.field.poly.fft.FFTUtils.calcMaxRoot;
import static org.arcanum.field.z.SinglePrecisionModularArithmetic.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class FFTPrime {

    private long q;   // the prime itself
    private double qinv;   // 1/((double) q)

    //   RootTable[j] = w^{2^{MaxRoot-j}},
    //                  where w is a primitive 2^MaxRoot root of unity
    //                  for q
    private long[] RootTable;

    // RootInvTable[j] = 1/RootTable[j] mod q
    private long[] RootInvTable;

    // TwoInvTable[j] = 1/2^j mod q
    private long[] TwoInvTable;

    // mulmod preconditioning data
    private double[] TwoInvPreconTable;


    public FFTPrime(long q, long w, boolean bigtab) {
        this.q = q;
        this.qinv = 1 / ((double) q);

        int mr = (int) calcMaxRoot(q);

        this.RootTable = new long[mr + 1];
        this.RootInvTable = new long[mr + 1];
        this.TwoInvTable = new long[mr + 1];
        this.TwoInvPreconTable = new double[mr + 1];

        RootTable[mr] = w;
        for (int j = mr - 1; j >= 0; j--)
            RootTable[j] = MulMod(RootTable[j + 1], RootTable[j + 1], q);

        RootInvTable[mr] = InvMod(w, q);
        for (int j = mr - 1; j >= 0; j--)
            RootInvTable[j] = MulMod(RootInvTable[j + 1], RootInvTable[j + 1], q);

        long t = InvMod(2, q);
        TwoInvTable[0] = 1;
        for (int j = 1; j <= mr; j++)
            TwoInvTable[j] = MulMod(TwoInvTable[j - 1], t, q);

        for (int j = 0; j <= mr; j++)
            TwoInvPreconTable[j] = PrepMulModPrecon(TwoInvTable[j], q, qinv);

//        if (bigtab)
//            info.bigtab.make(); TODO
    }


    public long getQ() {
        return q;
    }

    public double getQinv() {
        return qinv;
    }

    public long[] getRootTable() {
        return RootTable;
    }

    public long[] getRootInvTable() {
        return RootInvTable;
    }

    public long[] getTwoInvTable() {
        return TwoInvTable;
    }

    public double[] getTwoInvPreconTable() {
        return TwoInvPreconTable;
    }

}
