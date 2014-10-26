package org.arcanum.fhe.ap14.field;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Vector;
import org.arcanum.fhe.bv14.engine.BV14DMREngine;
import org.arcanum.fhe.bv14.generators.BV14DMRKeyGenerator;
import org.arcanum.fhe.bv14.params.BV14DMRKeyGenerationParameters;
import org.arcanum.fhe.bv14.params.BV14DMRKeyParameters;
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
    protected int bootCaseN, bootBigN;
    protected BigInteger bootCaseQ, bootBigQ;
    protected Vector bootCaseSecretKey, bootCaseSecretKeyDecomposed;

    protected Field[] bootFields;
    protected BV14DMREngine bv14DMREngine;
    protected Element[][] bsKeys;


    public AP14BootstrappableGSW14Field(SecureRandom random, int n, int k) {
        super(random, n, k);

        bootstrapGen();
    }


    protected void bootstrapGen() {
        // 1. Init
        bootstrapInitBootFields();

        // 2. Init DMR engine
        BV14DMRKeyParameters dmrKeyParameters = new BV14DMRKeyGenerator()
                .init(new BV14DMRKeyGenerationParameters(
                                random,
                                getDecryptionKey(),
                                bootCaseSecretKey
                        )
                ).generateKey();
        this.bv14DMREngine = new BV14DMREngine().init(dmrKeyParameters);

        // 3. Generate bootstrapping keys

        // Decompose bootCaseSecretKey
        bsKeys = new Element[t][d];
        for (int i = 0; i < t; i++) {
            for (int j = 0; j < d; j++) {
                bsKeys[i][j] = bootFields[i].newElement(bootCaseSecretKeyDecomposed.getAt(j).toBigInteger());
            }
        }
    }

    protected void bootstrapInitBootFields() {
        // 1.1
        // q needs to be of the form \prod r_i
        // where the r_i's are small and powers of distinct primes.
        // q will be the product of all maximal prime powers
        // that are bounded by O(\log \lambda)

        int x = 22;
        Apfloat xx = ApfloatUtils.newApfloat(x);
        List<Integer> rs = new ArrayList<Integer>();
        this.bootCaseQ = BigInteger.ONE;

        for (int i = 2; i <= x; i++) {
            if (!BigInteger.valueOf(i).isProbablePrime(12))
                continue;

            Apfloat ii = ApfloatUtils.newApfloat(i);

            Apfloat power = ApfloatMath.log(xx, ii).floor();
            Apfloat res = ApfloatMath.pow(ii, power);

            BigInteger resi = res.floor().toBigInteger();

            bootCaseQ = bootCaseQ.multiply(resi);
            rs.add(resi.intValue());
        }

        this.t = rs.size();
        this.bootCaseN = 80;


        // TODO: 3. Generate bootstrapping key

        // TODO: 4. Decompose s, depends on the modulus switching
        this.d = decryptionKey.getSize() * pk.getK();

        bootBigQ = BigInteger.valueOf(ApfloatUtils.log2(bootCaseQ));
        AP14GSW14Field baseField = new AP14GSW14Field(random, 4, bootBigQ.intValue(), s);
        bootFields = new Field[t];
        for (int i = 0; i < t; i++) bootFields[i] = new AP14HECyclicSBSymGroupField(baseField, rs.get(i));

        System.out.println(pk.getZq().getOrder());
        System.out.println("q = " + bootCaseQ);

        // 1.2
        // Q >> q must be sufficiently larger
        // then the error in the bootstrap's output ciphertext.
        //
        // Here, we interpret Q as Q = log_2(q)
        bootBigQ = BigInteger.valueOf(ApfloatUtils.log2(bootCaseQ));
        baseField = new AP14GSW14Field(random, 4, bootBigQ.intValue(), s);
        bootFields = new Field[t];
        for (int i = 0; i < t; i++) bootFields[i] = new AP14HECyclicSBSymGroupField(baseField, rs.get(i));

        System.out.println(pk.getZq().getOrder());
        System.out.println("q = " + bootCaseQ);

        //        AP14GSW14Field bootSmallField = new AP14GSW14Field(random, d, q);
    }

    protected AP14GSW14Element bootstrap(AP14GSW14Element element) {
        Vector ct = element.getViewColAt(element.getM() - 2);

        // 1. Switch ct to (d, q, bootSmallS)
        Element bootSmallCt = bv14DMREngine.processElements(ct);
//        System.out.printf("%s - %s\n", element.toBigInteger(), decrypt(switchedCt, getDecryptionKey(q)));


        // 2. TODO: Homomorphically compute an encryption of the inner product
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

            Element cursor = bsKeys[i][0].add(bootFields[i].newOneElement());
            for (int j = 1; j < d; j++) {
                // if c_j = 1
                cursor = bsKeys[i][j].add(cursor);
            }

            ip[i] = (Vector) cursor;
        }

        // TODO: 3. Homomorphically round

        return element;
    }

}
