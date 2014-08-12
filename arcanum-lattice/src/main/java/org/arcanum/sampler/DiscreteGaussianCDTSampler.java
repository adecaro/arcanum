package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.arcanum.Sampler;
import org.arcanum.common.concurrent.ThreadSecureRandom;
import org.arcanum.common.math.BigIntegerUtils;
import org.arcanum.field.floating.ApfloatUtils;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static org.arcanum.field.floating.ApfloatUtils.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DiscreteGaussianCDTSampler implements Sampler<BigInteger> {

    static final BigInteger L255 = BigInteger.valueOf(255);
    static final BigInteger MASK_INIT = L255.shiftLeft(64 - 8);
    static final int TAU = 13;

    static final double SIGMA_BIN_INV_LOWPREC = 1.17741002251547469101156932645969963; // sqrt(2 ln 2)
    static final double SIGMA_BIN_LOWPREC = 0.8493218002880190427215028341028896; //  sqrt(1/(2 ln 2))

    protected SecureRandom random;
    protected BigInteger[] cdt, cdtInvMin, cdtInvMax;
    protected boolean[] cdtDiffLessThanTwo;
    protected int[] curs;
    protected int cdtLength;


    public DiscreteGaussianCDTSampler(SecureRandom random, Apfloat gaussianParameter) {
        if (random == null)
            random = new SecureRandom();
        this.random = random;

        CDTDataBuilder.CDTData data = CDTDataBuilder.getInstance().compute(gaussianParameter);
        this.cdt = data.cdt;
        this.cdtInvMin = data.cdtInvMin;
        this.cdtInvMax = data.cdtInvMax;
        this.cdtLength = data.cdtLength;
        this.cdtDiffLessThanTwo = data.cdtDiffLessThanTwo;
        this.curs = data.curs;
    }

    public BigInteger sample() {
        SecureRandom random = ThreadSecureRandom.get();

        // TODO: avoid to use nextBoolean
        int r0 = random.nextInt(256);

        BigInteger min = cdtInvMin[r0];
        BigInteger max = cdtInvMax[r0];

        if (cdtDiffLessThanTwo[r0])
            return (random.nextBoolean()) ? cdtInvMin[r0] : cdtInvMin[r0].negate();

        int mask_index = 56;
        BigInteger r1 = BigInteger.valueOf(r0).shiftLeft(mask_index);
        BigInteger r2 = MASK_INIT;
//        int cur = min.add(max).shiftRight(1).intValue();

        int cur = curs[r0];

        while (true) {
            if (r1.compareTo(cdt[cur]) > 0)
                min = BigInteger.valueOf(cur);
            else if (r1.compareTo(cdt[cur].and(r2)) < 0)
                max = BigInteger.valueOf(cur);
            else {
                if (mask_index <= 0)
                    break;
                mask_index -= 8;

                r2 = r2.or(L255.shiftLeft(mask_index));
                r1 = r2.or(BigInteger.valueOf(random.nextInt(256)).shiftLeft(mask_index));
            }

            if (max.subtract(min).compareTo(BigIntegerUtils.TWO) < 0) {
                return (random.nextBoolean()) ? min : min.negate();
            }

            cur = min.add(max).shiftRight(1).intValue();
        }

        r2 = new BigInteger(64, random);
        while (true) {
            if (r1.compareTo(cdt[cur]) < 0 || ((r1.compareTo(cdt[cur]) == 0) && (r2.compareTo(cdt[cur + cdtLength])) < 0))
                max = BigInteger.valueOf(cur);
            else
                max = BigInteger.valueOf(cur); // TODO: this is strange!!!

            cur = min.add(max).shiftRight(1).intValue();

            if (max.subtract(min).compareTo(BigIntegerUtils.TWO) < 0)
                return (random.nextBoolean()) ? min : min.negate();
        }
    }


    public static class CDTDataBuilder {

        private static CDTDataBuilder INSTANCE = new CDTDataBuilder();

        public static CDTDataBuilder getInstance() {
            return INSTANCE;
        }

        protected Map<Apfloat, CDTData> dataMap;


        private CDTDataBuilder() {
            this.dataMap = new HashMap<Apfloat, CDTData>();
        }

        public CDTData compute(Apfloat gaussianParameter) {
            CDTData data = this.dataMap.get(gaussianParameter);
            if (data != null)
                return data;

            data = new CDTData(gaussianParameter);
            if (data.load())
                return data;

            BigInteger[] cdt, cdtInvMin, cdtInvMax;
            int cdtLength;

            Apfloat sigma = gaussianParameter.divide(SQRT_2PI);
            Apint k = gaussianParameter.multiply(newApfloat(SIGMA_BIN_INV_LOWPREC)).add(ApfloatUtils.IONE).truncate();

            // f = 2 sigma^2 = 2 k^2 1/(2ln(2)) = k^2/ln(2)
//            int k = (int) (SIGMA_BIN_INV_LOWPREC * gaussianParameter) + 1;

            //sqrt(2 ln 2)
//            int kk = ApfloatMath.sqrt(
//                    ApfloatUtils.TWO.multiply(
//                            ApfloatMath.log(ApfloatUtils.TWO)
//                    )
//            ).multiply(ApfloatUtils.newApint(gaussianParameter)).add(ApfloatUtils.IONE).truncate().intValue();

            Apfloat f = square(k).divide(ApfloatMath.log(ApfloatUtils.TWO));

            //sqrt(1/(2 ln 2))
//            int cdtl = ApfloatMath.sqrt(
//                    ApfloatUtils.ONE.divide(
//                        ApfloatUtils.TWO.multiply(
//                                ApfloatMath.log(ApfloatUtils.TWO)
//                        )
//                    )
//            ).multiply(ApfloatUtils.newApint(k))
//                    .multiply(ApfloatUtils.newApint(TAU))
//                    .add(ApfloatUtils.IONE).truncate().intValue();

            // compute normalization constant
            Apfloat t = ApfloatUtils.ZERO;
            cdtLength = k.multiply(newApfloat(SIGMA_BIN_LOWPREC)).add(IONE).truncate().intValue();

            for (int i = 1; i < cdtLength; i++) {
                Apfloat z = ApfloatUtils.newApfloat(i - 1);
                z = z.multiply(z);          // z =  (i-1)^2
                z = z.negate();             // z = -(i-1)^2
                z = z.divide(f);            // z = -(i-1)^2/f
                z = ApfloatMath.exp(z);     // z = exp(-(i-1)^2/f)
                if (i == 1)
                    z = z.divide(ApfloatUtils.TWO);

                t = t.add(z);
            }

            Apfloat ff = ApfloatMath.pow(ApfloatUtils.TWO, 64);
            Apfloat y = ApfloatUtils.ZERO;
            cdt = new BigInteger[cdtLength * 2];

            for (int i = 1; i < cdtLength; i++) {
                Apfloat z = ApfloatUtils.newApfloat(i - 1);
                z = z.multiply(z);          // z =  (i-1)^2
                z = z.negate();             // z = -(i-1)^2
                z = z.divide(f);            // z = -(i-1)^2/f
                z = ApfloatMath.exp(z);     // z = exp(-(i-1)^2/f)
                if (i == 1)
                    z = z.divide(ApfloatUtils.TWO);

                z = z.divide(t); // normalize
                y = y.add(z); // accumulate

                z = y;

                for (int j = 0; j < 2; j++) {
                    z = z.multiply(ff);

                    Apint zTruncate = z.truncate();
                    cdt[i + j * cdtLength] = zTruncate.toRadix(10).toBigInteger();

                    z = z.subtract(zTruncate);
                }
            }

            for (int j = 0; j < 2; j++)
                cdt[j * cdtLength] = BigInteger.ZERO;

            cdtInvMin = new BigInteger[256];
            cdtInvMax = new BigInteger[256];
            boolean[] cdtDiffLessThanTwo = new boolean[256];
            int[] curs = new int[256];

            int min = 0, max = 0;
            BigInteger mask = BigInteger.valueOf(255).shiftLeft(56);
            for (int i = 0; i < 256; i++) {
                BigInteger val = BigInteger.valueOf(i).shiftLeft(56);

                while (cdt[min + 1].compareTo(val) < 0)
                    min++;

                while ((max + 1 < cdtLength) && (cdt[max].and(mask).compareTo(val) <= 0))
                    max++;

                cdtInvMin[i] = BigInteger.valueOf(min);
                cdtInvMax[i] = BigInteger.valueOf(max);

                cdtDiffLessThanTwo[i] = (cdtInvMax[i].subtract(cdtInvMin[i]).compareTo(BigIntegerUtils.TWO) < 0);
                curs[i] = cdtInvMin[i].add(cdtInvMax[i]).shiftRight(1).intValue();
            }

            data = new CDTData(
                    gaussianParameter,
                    cdt, cdtInvMin, cdtInvMax, cdtLength,
                    cdtDiffLessThanTwo, curs
                    );
            this.dataMap.put(gaussianParameter, data);

            return data;
        }


        public class CDTData {
            Apfloat gaussianParameter;
            BigInteger[] cdt, cdtInvMin, cdtInvMax;
            boolean[] cdtDiffLessThanTwo;
            int[] curs;
            int cdtLength;

            public CDTData(Apfloat gaussianParameter,
                           BigInteger[] cdt,
                           BigInteger[] cdtInvMin,
                           BigInteger[] cdtInvMax,
                           int cdtLength,
                           boolean[] cdtDiffLessThanTwo,
                           int[] curs) {
                this.gaussianParameter = gaussianParameter;
                this.cdt = cdt;
                this.cdtInvMin = cdtInvMin;
                this.cdtInvMax = cdtInvMax;
                this.cdtLength = cdtLength;
                this.cdtDiffLessThanTwo =  cdtDiffLessThanTwo;
                this.curs = curs;

                store();
            }

            public CDTData(Apfloat gaussianParameter) {
                this.gaussianParameter = gaussianParameter;
            }

            public boolean load() {
                try {
                    if (!new File(gaussianParameter.toRadix(10).toString(true)).exists())
                        return false;

                    BufferedReader reader = new BufferedReader(
                            new FileReader(gaussianParameter.toRadix(10).toString(true))
                    );

                    cdtLength = Integer.valueOf(reader.readLine());
                    cdt = new BigInteger[cdtLength *2];
                    for (int i = 0; i < cdt.length; i++) {
                        cdt[i] = new BigInteger(reader.readLine());
                    }

                    int length = Integer.valueOf(reader.readLine());
                    cdtInvMin = new BigInteger[length];
                    for (int i = 0; i < length; i++) {
                        cdtInvMin[i] = new BigInteger(reader.readLine());
                    }

                    cdtInvMax = new BigInteger[length];
                    for (int i = 0; i < length; i++) {
                        cdtInvMax[i] = new BigInteger(reader.readLine());
                    }

                    cdtDiffLessThanTwo =  new boolean[length];
                    for (int i = 0; i < length; i++) {
                        cdtDiffLessThanTwo[i] = Boolean.valueOf(reader.readLine());
                    }

                    curs =  new int[length];
                    for (int i = 0; i < length; i++) {
                        curs[i] = Integer.valueOf(reader.readLine());
                    }

                    reader.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return true;
            }

            public void store() {
                try {

                    PrintStream stream = new PrintStream(new FileOutputStream(
                            gaussianParameter.toRadix(10).toString(true)
                    ));

                    stream.println(cdtLength);
                    for (BigInteger aCDT : cdt) {
                        stream.println(aCDT);
                    }

                    stream.println(cdtInvMin.length);
                    for (BigInteger v : cdtInvMin) {
                        stream.println(v);
                    }
                    for (BigInteger v : cdtInvMax) {
                        stream.println(v);
                    }
                    for (boolean v : cdtDiffLessThanTwo) {
                        stream.println(v);
                    }
                    for (int v : curs) {
                        stream.println(v);
                    }

                    stream.flush();
                    stream.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

}
