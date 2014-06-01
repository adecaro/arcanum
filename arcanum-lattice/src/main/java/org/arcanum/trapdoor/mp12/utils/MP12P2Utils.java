package org.arcanum.trapdoor.mp12.utils;

import org.apfloat.Apfloat;
import org.apfloat.Apint;
import org.arcanum.Sampler;
import org.arcanum.field.floating.ApfloatUtils;
import org.arcanum.sampler.SamplerFactory;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.apfloat.ApfloatMath.pow;
import static org.apfloat.ApfloatMath.sqrt;
import static org.arcanum.field.floating.ApfloatUtils.*;

/**
 * TODO: continue with java docs
 * Trapdoor for Lattices: Simpler, Tighter, Faster, Smaller.
 * Micciancio and Peikert
 *
 * <p/>
 * ----------------------------
 * Primitive Lattices (G,S)
 * ----------------------------
 * <p/>
 * b                =        2
 * k
 * q                =        2^k
 * n               <=        2^14
 * \espilon        >=        2^(-100)
 * det(S)           =        q^n
 * ||S||            =        sqrt(5)
 * \tilde S         =        2I
 * ||\tilde S||     =        2
 * r(n)             =        w(sqrt(log n))
 * r               \approx   sqrt(ln(2/\epsilon)/\pi) <= 4.721
 * s.p.             =        2r                       <= 10
 * s               >=        2r                        = 10
 * G               \in       \Z_qˆ{n\times w}
 * \sqrt(\Sigma_G)  =        sI
 * s_1(\Sigma_G)    =        ||\bar S||^2              = 4
 * <p/>
 * ----------------------------
 * GenTrap
 * ----------------------------
 * <p/>
 * w             =        nk
 * \bar m        =        2n
 * m             =        w + \bar m                = nk + 2n = n(k+2)
 * s_{GenTrap}   =        \alpha q                  > sqrt(n)
 * <p/>
 * D             =        D^{\bar m \times w}_{\Z,s_{GenTrap}}
 * \bar A       \in       \Z_qˆ{n\times \bar m}
 * H            \in       \Z_qˆ{n\times n}          = I_n
 * A            \in       \Z_qˆ{n\times m}          = [\bar A | HG - \bar A R]
 * R            \in       \Z_qˆ{\bar m \times w}
 * <p/>
 * <p/>
 * ----------------------------
 * Gaussian Sampling
 * ----------------------------
 * <p/>
 * C             <= 1/sqrt(2*pi)
 * t              = 1
 * \alpha q      >= 2 \sqrt(n)
 * s_1(R)        <= C \cdot (\alpha q) \cdot (sqrt(2n) + \sqrt(nk) + t )
 * <p/>
 * s             >= \sqrt(s_1(R)^2+1) \cdot \sqrt(s_1(\Sigma_G)+2) \cdot r
 * = \sqrt(s_1(R)^2+1) \cdot \sqrt(6) \cdot r
 * <p/>
 * <p/>
 * COV            = r^2 [R || I]^T [R^T || I]
 * \Sigma_P       = s^2I - COV
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12P2Utils {

    public static int TAU = 13;
    public static Apint iTAU = newApint(TAU);

    /**
     * Randomized-rounding parameter
     * r \approx sqrt(ln(2/epsilon)/pi)
     * for epsilon \approx 2^-90.
     */
    public static Apfloat RRP = convert(new Apfloat("4.5"));
    public static Apfloat RRP_SQUARE = square(RRP);


    public static Sampler<BigInteger> getLWENoiseSampler(SecureRandom random, int n) {
        return SamplerFactory.getInstance().getDiscreteGaussianSampler(random, getLWENoiseParameter(n));
    }

    protected static Apfloat getLWENoiseParameter(int n) {
        return SQRT_TWO.multiply(
                ITWO.multiply(sqrt(newApfloat(n)))
        ).multiply(RRP_SQUARE).multiply(RRP);
    }

    public static Apfloat getLWENoiseParameter(int n, Apfloat rrp) {
        return SQRT_TWO.multiply(
                ITWO.multiply(sqrt(newApfloat(n)))
        ).multiply(rrp).multiply(rrp).multiply(rrp);
    }


    public static Apfloat getS1R(Apfloat gaussianParamenter, int n, int m) {
        return gaussianParamenter.multiply(
                ApfloatUtils.sqrt(n).add(ApfloatUtils.sqrt(m)).add(ApfloatUtils.IONE)
        ).divide(SQRT_TWO_PI);
    }

    protected static Apfloat getSqrtS1RSquarePlusOne(int n, int m) {
        return sqrt(square(getS1R(getLWENoiseParameter(n), n, m)).add(IONE));
    }

    public static void testLWENoiseSampler(int n, int k) {
        int barM = 2 * n;
        int w = n * k;
        int m = barM + w;

        // 2 sqrt(n) / q
        Apfloat q = pow(ITWO, k);
        Apfloat oneOverQ = ONE.divide(q);
        Apfloat alpha = getLWENoiseParameter(n).divide(q);
        Apfloat oneOverAlpha = ONE.divide(alpha);
        Apfloat oneOverRRP = ONE.divide(RRP);

        System.out.println(ApfloatUtils.toString(oneOverQ));
        System.out.println(ApfloatUtils.toString(alpha));
        System.out.println(ApfloatUtils.toString(oneOverRRP));
        System.out.println(ApfloatUtils.toString(oneOverAlpha));

        System.out.println(oneOverQ.compareTo(alpha));
        System.out.println(alpha.compareTo(oneOverRRP));

        System.out.println(oneOverAlpha.compareTo(
                IFOUR.multiply(getSqrtS1RSquarePlusOne(barM, w)).multiply(RRP)
        ));
//        where 1/α ≥ 2 ∥B∥ s · ω(√log n) = 4 s ω(√log n)
//        getS1R()
//        ONE.divide(alpha)
    }
}
