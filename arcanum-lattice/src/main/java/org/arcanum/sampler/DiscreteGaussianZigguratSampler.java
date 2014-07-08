package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.apfloat.Apint;
import org.apfloat.ApintMath;
import org.arcanum.Sampler;
import org.arcanum.field.floating.ApfloatUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.apfloat.ApfloatMath.*;
import static org.apfloat.ApfloatMath.log;
import static org.apfloat.ApfloatMath.sqrt;
import static org.arcanum.field.floating.ApfloatUtils.*;
import static org.arcanum.field.floating.ApfloatUtils.pi;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DiscreteGaussianZigguratSampler implements Sampler<BigInteger> {

    protected SecureRandom random;
    protected int m;                    // number of rectangles
    protected Apfloat sigma;
    protected int omega;
    protected Apfloat[] rectxs;
    protected Apfloat[] rectys;


    public DiscreteGaussianZigguratSampler(SecureRandom random, int m, Apfloat gaussianParameter) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.m = m;
        this.sigma = gaussianParameter.divide(SQRT_2PI);;
        this.omega = ApfloatUtils.precision;

        build();
    }

    public DiscreteGaussianZigguratSampler(SecureRandom random, Apfloat gaussianParameter) {
        this(random, 30, gaussianParameter);
    }


    public BigInteger sample() {
        Apfloat res;
        while (true) {
            int i = 1 + random.nextInt(m);  //< sample rectangle uniformly, rectangle i <- {1,...,m}
            Apint xurb = rectxs[i].add(IONE).truncate();
            res = newApint(random.nextInt(xurb.intValue())); //< res <- [0, \floor{x_{i}}]

            if (res.compareTo(IZERO) != 0 && res.compareTo(rectxs[i - 1]) <= 0)
                break;
            else {
                // the case x=0 is special due to 0=-0, therefore we have to
                // halve the prob. for 0 which results in 1/2; so with p=1/2
                // p gets accepted and with p=1/2 rejected -> "coin toss"
                if (res.compareTo(IZERO) == 0) {
                    if (random.nextInt(2) == 0)
                        break;
                } else {
                    Apint yfactor = ApintMath.pow(ITWO, omega);
                    Apint yprime = newApint(random.nextInt(yfactor.intValue()));

                    Apint y = yprime.multiply(rectys[i - 1].subtract(rectys[i])).truncate();

                    // y <- [0,\floor{2^{\omega} (\rho_{\sigma}(x_{i-1}) - \rho_{\sigma}(x_{i}))}]
                    if (y.compareTo(yfactor.multiply((rho(sigma, res).subtract(rectys[i])))) <= 0)
                        break;
                }
            }
        }

        int s = 1 - 2 * random.nextInt(2); //< sign <- +/- 1
        return res.multiply(newApint(s)).truncate().toBigInteger();
    }



    /**
     * compute a possible partition
     * arguments:
     * - xis = storage for x-values
     * - rhos = storage for y-values
     * - c = area of rectangles
     * - m = number of rectangles
     * - sigma = "std. deviation" of (discrete Gaussian) distribution
     */
    static Apfloat compute(Apfloat[] xis, Apfloat[] rhos, Apfloat c, Apfloat m, Apfloat sigma) {

        //< m = #rectangles, thus we need m+1 x-values ("x_i's" or "xis")
        int em = m.intValue() + 1;

        //< helping variables for computation
        Apfloat om = IONE.divide(m), m2 = newApint(-2), o2 = newApint(1).divide(newApint(2));

        //< area of each rectangle
        Apfloat area = sigma
                .multiply(om)
                .multiply(sqrt(pi().divide(ITWO)))
                .multiply(c);

        //< set the largest x-value to 13*sigma
        xis[em - 1] = sigma.multiply(newApint(13));

        //< rhos = coApfloatesponding y-values to the x_i's
        //< manually set y-value for largest x-value (rounded to the next-largest integer)
        rhos[em - 1] = rho(sigma, xis[em - 1].truncate().add(IONE));
        Apfloat sqrtv = m2.multiply(log(area.divide(xis[em - 1].truncate().add(IONE))));
        if (sqrtv.compareTo(ZERO) < 0)
            return newApint(-1);

        //< manually compute 2nd largest x-value (and coApfloatesponding y-value)
        xis[em - 2] = sigma.multiply(sqrt(sqrtv));
        rhos[em - 2] = rho(sigma, xis[em - 2]);

        //< compute the other x- and y-values iteratively from back to front
        for (int i = em - 3; i > 0; i--) {
            sqrtv = m2.multiply(log(area.divide(xis[i + 1].truncate().add(IONE)).add(rho(sigma, xis[i + 1]))));
            if (sqrtv.compareTo(ZERO) < 0)
                return newApint(-1);

            xis[i] = sigma.multiply(sqrt(sqrtv));
            rhos[i] = exp(o2.negate().multiply(pow(xis[i].divide(sigma), 2)));
        }

        //< compute the y-value for x=0 and output it (as "quality" of the partition)
        rhos[0] = area.divide(xis[1].truncate().add(IONE)).add(rho(sigma, xis[1]));
        return rhos[0];
    }


    /**
     * computes e^{-1/2 (x/sigma)^2} *
     */
    public static Apfloat rho(Apfloat sigma, Apfloat x) {
//        return exp(-power(x/sigma, 2)/2.0);
        return exp(pow(x.divide(sigma), 2).negate().divide(TWO));
    }

    /**
     * main function to compute a valid partition
     * arguments:
     * - m = number of rectangles
     * - sigma = "std. deviation" of (discrete Gaussian) distribution
     * - precision of the computation
     */
    protected void build() {

        //< set precisions
        Apfloat prec = pow(TWO, -precision); //< max. approximation eApfloator
        Apfloat prec2 = prec.multiply(prec);
//        Apfloat::SetOutputPrecision (precision); //< output-precision of numbers

        //< initialize different variables
        Apfloat[] xis = new Apfloat[m + 1];
        Apfloat[] bestxis = new Apfloat[m + 1];
        for (int i = 0; i <= m; i++)
            bestxis[i] = newApint(-1);

        Apfloat[] rhos = new Apfloat[m + 1];
        Apfloat mm = newApfloat(m);
        Apfloat c = IONE.add(IONE.divide(mm));
        Apfloat tailcut = sigma.multiply(newApint(13));

        /***\
         start program
         \***/
        if (mm.compareTo(IONE) == 0) {
//            cout << precision << " " << mm << " " << sigma << " 1 0 " << tailcut << endl;
            return;
        }

        //< mm != 1
        Apfloat bestdiff = newApint(3);

        //< increase right bound until reaching 14*sigma and compute possible partition(s)
        while (tailcut.compareTo(sigma.multiply(newApint(14))) < 0) {
            xis[m] = tailcut;
            Apfloat cu = newApint(0), cl = newApint(1), cc;
            Apfloat difference = newApint(-1), lastdiff = newApint(-2);

            System.out.println("tailcut = " + ApfloatUtils.toString(tailcut));

            //< try to minimize the distance of y0 to 1 (y0 = y-value to x=0)
            while (difference.compareTo(ZERO) < 0 || (abs(difference).compareTo(prec) > 0 && abs(difference.subtract(lastdiff)).compareTo(prec) > 0)) {
                cc = c;
                lastdiff = difference;
                difference = compute(xis, rhos, c, mm, sigma).subtract(newApint(1));

                System.out.println("difference = " + ApfloatUtils.toString(difference));

                if (difference.compareTo(newApint(-2)) == 0)
                    break; //< in case of any failure in partition-computation
                if (difference.compareTo(ZERO) >= 0) //< if possible partition found, renew best solution,
                //< since difference to 1 is smaller than before
                {
                    for (int i = 0; i <= m; i++) bestxis[i] = xis[i];
                    cc = c;
                    cu = c;
                    bestdiff = difference;
                }
                //< do some tricks with the "area" c in order to improve solution
                else {
                    cl = c;
                }
                if (cu.compareTo(cl) < 0) {
                    c = c.add(newApint(1).divide(mm));
                } else {
                    c = (cu.add(cl)).divide(newApint(2));
                }
                if (c.compareTo(newApint(11)) >= 0) break;
                if (difference.compareTo(lastdiff) == 0) break;

                System.out.println("c = " + ApfloatUtils.toString(c));
            }

            //< if while-loop did not improve anything, increase tailcut and go to next iteration
            if (difference.compareTo(ZERO) < 0 || (abs(difference).compareTo(prec) > 0 && abs(difference.subtract(lastdiff)).compareTo(prec) > 0)) {
                tailcut.add(IONE);
            }
            //< else stop the partition-computation
            else {
                break;
            }
        }

        rectxs = new Apfloat[m + 1];
        rectys = new Apfloat[m + 1];

        //< if valid solution exists, output it to terminal and return 0 (success)
        if (bestxis[m].compareTo(newApint(-1)) != 0) {
            // output precision, number of rectangles mm, (std. deviation) sigma, and y0>=1 (the value for x=0)
            System.out.println("precision = " + ApfloatUtils.toString(mm));
            System.out.println("sigma = " + ApfloatUtils.toString((newApfloat(1).add(bestdiff))));

            // output the "x_i's", i.e. values on the x-axis
            for (int i = 0; i <= m; i++) {
                System.out.println(ApfloatUtils.toString(bestxis[i]));
                if (i != 0) {
                    rectxs[i] = bestxis[i].truncate();
                    rectys[i] = rho(sigma, bestxis[i]);
                } else {
                    rectxs[0] = IZERO;
                    rectys[0] = bestdiff.add(IONE);
                }
            }
            System.out.println();
            return;


        }

        throw new IllegalStateException("FAILURE");
    }

}