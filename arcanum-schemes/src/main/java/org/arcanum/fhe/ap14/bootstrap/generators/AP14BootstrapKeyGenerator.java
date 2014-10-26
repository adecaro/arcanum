package org.arcanum.fhe.ap14.bootstrap.generators;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Vector;
import org.arcanum.common.cipher.generators.ElementKeyGenerator;
import org.arcanum.fhe.ap14.bootstrap.params.AP14BootstrapKeyGenerationParameters;
import org.arcanum.fhe.ap14.bootstrap.params.AP14BootstrapKeyParameters;
import org.arcanum.fhe.bv14.engine.BV14DMREngine;
import org.arcanum.field.floating.ApfloatUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * The scheme to be bootstrapped must have binary ciphertexts in {0, 1}
 * d and secret keys in Zd q for some
 * dimension d and modulus q that should be made as small as possible (q, d = O˜(λ) are possible), and a
 * decryption function of the form Decs(c) = f(hs, ci) ∈ {0, 1} for some arbitrary function f : Zq → {0, 1}.
 * We can bootstrap ciphertext of dimension d and modulo q
 * (= the product of all maximal prime-powers ri that are bounded by O(log \lambda)).
 *
 * Thus, we need a dimension-modulo reduction engine setup to do so.
 *
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14BootstrapKeyGenerator implements ElementKeyGenerator<AP14BootstrapKeyGenerationParameters, AP14BootstrapKeyParameters> {

    // Bootstrapping fields
    protected int t, d, n;
    protected BigInteger q, Q;
    protected Vector bootCaseSecretKey, bootCaseSecretKeyDecomposed;

    protected Field[] bootFields;
    protected BV14DMREngine bv14DMREngine;
    protected Element[][] bsKeys;

    @Override
    public AP14BootstrapKeyGenerator init(AP14BootstrapKeyGenerationParameters keyGenerationParameters) {
        initDimensionModulo();

        initHEPerm();

        // TODO: 3. Generate bootstrapping key

        // TODO: 4. Decompose s, depends on the modulus switching
/*
        Q = BigInteger.valueOf(ApfloatUtils.log2(q));
        AP14GSW14Field baseField = new AP14GSW14Field(random, 4, Q.intValue(), s);
        bootFields = new Field[t];
        for (int i = 0; i < t; i++) bootFields[i] = new AP14HECyclicSBSymGroupField(baseField, rs.get(i));

        System.out.println(pk.getZq().getOrder());
        System.out.println("q = " + q);

        // 1.2
        // Q >> q must be sufficiently larger
        // then the error in the bootstrap's output ciphertext.
        //
        // Here, we interpret Q as Q = log_2(q)
        Q = BigInteger.valueOf(ApfloatUtils.log2(q));
        baseField = new AP14GSW14Field(random, 4, Q.intValue(), s);
        bootFields = new Field[t];
        for (int i = 0; i < t; i++) bootFields[i] = new AP14HECyclicSBSymGroupField(baseField, rs.get(i));

        System.out.println(pk.getZq().getOrder());
        System.out.println("q = " + q);

        //        AP14GSW14Field bootSmallField = new AP14GSW14Field(random, d, q);


        // 2. Init DMR engine
        BV14DMRKeyParameters dmrKeyParameters = new BV14DMRKeyGenerator()
                .init(new BV14DMRKeyGenerationParameters(
                                random,
                                getDecryptionKey(),
                                bootCaseSecretKey
                        )
                ).generateKey();
        this.bv14DMREngine = new BV14DMREngine().init(dmrKeyParameters);

*/
        return this;
    }

    @Override
    public AP14BootstrapKeyParameters generateKey() {
        // Decompose bootCaseSecretKey
        bsKeys = new Element[t][d];
        for (int i = 0; i < t; i++) {
            for (int j = 0; j < d; j++) {
                bsKeys[i][j] = bootFields[i].newElement(bootCaseSecretKeyDecomposed.getAt(j).toBigInteger());
            }
        }

//        return new AP14BootstrapKeyParameters();
        return null;
    }



    protected void initDimensionModulo() {
        // q needs to be of the form \prod r_i
        // where the r_i's are small and powers of distinct primes.
        // q will be the product of all maximal prime powers
        // that are bounded by O(\log \lambda)

        int x = 22;
        Apfloat xx = ApfloatUtils.newApfloat(x);
        List<Integer> rs = new ArrayList<Integer>();
        this.q = BigInteger.ONE;

        for (int i = 2; i <= x; i++) {
            if (!BigInteger.valueOf(i).isProbablePrime(12))
                continue;

            Apfloat ii = ApfloatUtils.newApfloat(i);

            Apfloat power = ApfloatMath.log(xx, ii).floor();
            Apfloat res = ApfloatMath.pow(ii, power);

            BigInteger resi = res.floor().toBigInteger();

            this.q = this.q.multiply(resi);
            rs.add(resi.intValue());
        }

        this.t = rs.size();

        //
//        this.d = 80;
//        this.d = decryptionKey.getSize() * pk.getK();
    }

    protected void initHEPerm() {

    }

}
