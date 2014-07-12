package org.arcanum.fhe.gsw14.field;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Vector;
import org.arcanum.field.floating.ApfloatUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14BootstrappableGSW14Field extends AP14GSW14Field {

    // Bootstrapping fields
    protected int t, d;
    protected BigInteger bootSmallQ, bootBigQ;
    protected Field[] bootFields;
    protected Element[][] bootCts;


    public AP14BootstrappableGSW14Field(SecureRandom random, int n, int k) {
        super(random, n, k);

        bootstrapGen();
    }


    protected void bootstrapGen() {
        // 1. Generate bootSmallQ, bootBigQ and relative fields

        // bootSmallQ needs to be of the form \prod r_i
        // where the r_i's are small and powers of distinct primes.
        // bootSmallQ will be the product of all maximal prime powers
        // that are bounded by O(\log \lamda)
        int x = 22;
        Apfloat xx = ApfloatUtils.newApfloat(x);
        List<Integer> rs = new ArrayList<Integer>();
        this.bootSmallQ = BigInteger.ONE;

        for (int i = 2; i <= x; i++) {
            if (!BigInteger.valueOf(i).isProbablePrime(12))
                continue;

            Apfloat ii = ApfloatUtils.newApfloat(i);

            Apfloat power = ApfloatMath.log(xx, ii).floor();
            Apfloat res = ApfloatMath.pow(ii, power);

            BigInteger resi = res.floor().toBigInteger();

            bootSmallQ = bootSmallQ.multiply(resi);
            rs.add(resi.intValue());
        }
        this.t = rs.size();

        System.out.println("sq = " + bootSmallQ);
        System.out.println("q  = " + pk.getZq().getOrder());

        // bootBigQ >> bootSmallQ must be sufficiently larger
        // then the error in the bootstrap's output ciphertext.
        //
        // Here, we interpret bootBigQ as bootBigQ = log_2(bootSmallQ)
        bootBigQ = BigInteger.valueOf(ApfloatUtils.log2(bootSmallQ));
        AP14GSW14Field baseField = new AP14GSW14Field(random, 4, bootBigQ.intValue(), s);
        System.out.println("Q  = " + baseField.getPk().getZq().getOrder());
        bootFields = new Field[t];
        for (int i = 0; i < t; i++) {
            bootFields[i] = new AP14HECyclicSBSymGroupField(baseField, rs.get(i));
        }

        // TODO: 2. Init Modulus switching to bootSmallQ

        

        // TODO: 3. Generate bootstrapping key

        // TODO: 4. Decompose s, depends on the modulus switching
        this.d = sDec.getSize() * pk.getK();

        bootCts = new Element[t][d];
        for (int i = 0; i < t; i++) {
            for (int j = 0, d = 0, size = sDec.getSize(); j < size; j++) {
                BigInteger value = BigInteger.ONE;
                Element cursor = sDec.getAt(j).duplicate();
                for (int k = 0; k < pk.getK(); k++) {
                    BigInteger s = cursor.duplicate().mul(value).toBigInteger();

                    bootCts[i][d++] = bootFields[i].newElement(s);
                }
            }
        }
    }

    protected AP14GSW14Element bootstrap(AP14GSW14Element element) {
        Vector ct = element.getViewColAt(element.getM() - 2);


        // TODO: 1. Modulus switching: Switch ct's modulo to bootSmallQ

        // 2. Homomorphically compute an encryption of the inner product
//        Vector ciphertext = element.getViewColAt(element.getM() - 2);
//        for (int i = 0, base = 0; i < n; i++) {
//
//            BigInteger u = (ciphertext.isZeroAt(i)) ? BigInteger.ZERO : ciphertext.getAt(i).toBigInteger();
//
//            for (int j = 0; j < k; j++) {
//                BigInteger xj = (u.testBit(0)) ?  BigInteger.ONE : BigInteger.ZERO;
//
//                r.getAt(base + j).set(xj);
//
//                u  = u.subtract(xj).shiftRight(1);
//            }
//
//            base += k;
//        }

        Vector[] ip = new Vector[t];
        for (int i = 0; i < t; i++) {

            Element cursor = bootCts[i][0].add(bootFields[i].newOneElement());
            for (int j = 1; j < d; j++) {
                // if c_j = 1
                cursor = bootCts[i][j].add(cursor);
            }

            ip[t] = (Vector) cursor;
        }

        // TODO: 3. Homomorphically round

        return element;
    }

}
