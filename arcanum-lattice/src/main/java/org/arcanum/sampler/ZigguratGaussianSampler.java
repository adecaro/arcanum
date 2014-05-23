package org.arcanum.sampler;

import org.arcanum.Sampler;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ZigguratGaussianSampler implements Sampler<Double> {

    long MAX = 0xffffffffl;

    /// <summary>
    /// Number of blocks.
    /// </summary>
    final
    int blockCount = 128;
    /// <summary>
    /// Right hand x coord of the base rectangle, thus also the left hand x coord of the tail 
    /// (pre-determined/computed for 128 blocks).
    /// </summary>
    final
    double R = 3.442619855899;
    /// <summary>
    /// Area of each rectangle (pre-determined/computed for 128 blocks).
    /// </summary>
    final
    double A = 9.91256303526217e-3;
    /// <summary>
    /// Scale factor for converting a UInt with range [0,0xffffffff] to a double with range [0,1].
    /// </summary>
    final
    double __UIntToU = 1.0 / (double) MAX;

    Random random;

    // x[i] and y[i] describe the top-right position ox rectangle i.
    double[] x;
    double[] y;

    // The proprtion of each segment that is entirely within the distribution, expressed as uint where 
    // a value of 0 indicates 0% and uint.MaxValue 100%. Expressing this as an integer allows some floating
    // points operations to be replaced with integer ones.
    long[] xComp;

    // Useful precomputed values.
    // Area A divided by the height of B0. Note. This is *not* the same as x[i] because the area
    // of B0 is A minus the area of the distribution tail.
    double aDivY0;


    /// <summary>
    /// Construct with a default RNG source.
    /// </summary>
    public ZigguratGaussianSampler() {
        this(new SecureRandom());
    }

    /// <summary>
    /// Construct with the provided RNG source.
    /// </summary>
    public ZigguratGaussianSampler(SecureRandom rng) {
        random = rng;

        // Initialise rectangle position data. 
        // x[i] and y[i] describe the top-right position ox Box i.

        // Allocate storage. We add one to the length of x so that we have an entry at x[_blockCount], this avoids having
        // to do a special case test when sampling from the top box.
        x = new double[blockCount + 1];
        y = new double[blockCount];

        // Determine top right position of the base rectangle/box (the rectangle with the Gaussian tale attached). 
        // We call this Box 0 or B0 for short.
        // Note. x[0] also describes the right-hand edge of B1. (See diagram).
        x[0] = R;
        y[0] = GaussianPdfDenorm(R);

        // The next box (B1) has a right hand X edge the same as B0. 
        // Note. B1's height is the box area divided by its width, hence B1 has a smaller height than B0 because
        // B0's total area includes the attached distribution tail.
        x[1] = R;
        y[1] = y[0] + (A / x[1]);

        // Calc positions of all remaining rectangles.
        for (int i = 2; i < blockCount; i++) {
            x[i] = GaussianPdfDenormInv(y[i - 1]);
            y[i] = y[i - 1] + (A / x[i]);
        }

        // For completeness we define the right-hand edge of a notional box 6 as being zero (a box with no area).
        x[blockCount] = 0.0;

        // Useful precomputed values.
        aDivY0 = A / y[0];
        xComp = new long[blockCount];

        // Special case for base box. xComp[0] stores the area of B0 as a proportion of R
        // (recalling that all segments have area A, but that the base segment is the combination of B0 and the distribution tail).
        // Thus -xComp[0[ is the probability that a sample point is within the box part of the segment.
        xComp[0] = (long) (((R * y[0]) / A) * (double) MAX);

        for (int i = 1; i < blockCount - 1; i++) {
            xComp[i] = (long) ((x[i + 1] / x[i]) * (double) MAX);
        }
        xComp[blockCount - 1] = 0;  // Shown for completeness.

        // Sanity check. Test that the top edge of the topmost rectangle is at y=1.0.
        // Note. We expect there to be a tiny drift away from 1.0 due to the inexactness of floating
        // point arithmetic.
        if (!(Math.abs(1.0 - y[blockCount - 1]) < 1e-10))
            throw new IllegalStateException();
    }


    /// <summary>
    /// Get the next sample value from the gaussian distribution.
    /// </summary>
    public Double sample() {
        for (; ; ) {
            // Select box at random.
            byte[] bytes = new byte[1];
            random.nextBytes(bytes);
            byte u = bytes[0];
            int i = (int) (u & 0x7F);
//            System.out.println("i = " + i);
            double sign = ((u & 0x80) == 0) ? -1.0 : 1.0;

            // Generate uniform random value with range [0,0xffffffff].
            long u2 = Math.abs(random.nextLong()) % MAX;

            // Special case for the base segment.
            if (0 == i) {
                if (u2 < xComp[0]) {   // Generated x is within R0.
                    return u2 * __UIntToU * aDivY0 * sign;
                }
                // Generated x is in the tail of the distribution.
                return SampleTail() * sign;
            }

            // All other segments.
            if (u2 < xComp[i]) {   // Generated x is within the rectangle.
//                System.out.println("INSIDE");
                return u2 * __UIntToU * x[i] * sign;
            }

            // Generated x is outside of the rectangle.
            // Generate a random y coordinate and test if our (x,y) is within the distribution curve.
            // This execution path is relatively slow/expensive (makes a call to Math.Exp()) but relatively rarely executed,
            // although more often than the 'tail' path (above).
            double x = u2 * __UIntToU * this.x[i];
//            System.out.println("OUTSIDE");

            if (y[i - 1] + ((y[i] - y[i - 1]) * random.nextDouble()) < GaussianPdfDenorm(x)) {
                return x * sign;
            }
        }
    }

    /*
    /// <summary>
    /// Get the next sample value from the gaussian distribution.
    /// </summary>
    /// <param name="mu">The distribution's mean.</param>
    /// <param name="mu">The distribution's standard deviation.</param>
    public double NextSample(double mu, double sigma) {
        return mu + (NextSample() * sigma);
    }
*/

    /// <summary>
    /// Sample from the distribution tail (defined as having x >= R).
    /// </summary>
    /// <returns></returns>
    private double SampleTail() {
        double x, y;
        do {
            // Note. we use NextDoubleNonZero() because Log(0) returns NaN and will also tend to be a very slow execution path (when it occurs, which is rarely).
            x = -Math.log(random.nextDouble()) / R;
            y = -Math.log(random.nextDouble());
        }
        while (y + y < x * x);
        return R + x;
    }

    /// <summary>
    /// Gaussian probability density function, denormailised, that is, y = e^-(x^2/2).
    /// </summary>
    private double GaussianPdfDenorm(double x) {
        return Math.exp(-(x * x / 2.0));
    }

    /// <summary>
    /// Inverse function of GaussianPdfDenorm(x)
    /// </summary>
    private double GaussianPdfDenormInv(double y) {
        // Operates over the y range (0,1], which happens to be the y range of the pdf, 
        // with the exception that it does not include y=0, but we would never call with 
        // y=0 so it doesn't matter. Remember that a Gaussian effectively has a tail going
        // off into x == infinity, hence asking what is x when y=0 is an invalid question
        // in the context of this class.
        return Math.sqrt(-2.0 * Math.log(y));
    }


    public static void main(String[] args) {
        ZigguratGaussianSampler sampler = new ZigguratGaussianSampler();
        while (true) {
            double d = sampler.sample();
            System.out.println("d = " + d);
        }
    }
}
