package org.arcanum.field.poly;

import org.arcanum.Field;
import org.arcanum.field.base.AbstractFieldOver;
import org.arcanum.field.poly.fft.FFTInfo;

import java.security.SecureRandom;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PolyField<F extends Field> extends AbstractFieldOver<F, PolyElement> {

    protected FFTInfo fftInfo;


    public PolyField(SecureRandom random, F targetField) {
        super(random, targetField);

        this.fftInfo = new FFTInfo(targetField.getOrder());
    }

    public PolyField(F targetField) {
        this(new SecureRandom(), targetField);
    }


    public PolyElement newElement() {
        return new PolyElement(this);
    }

    public PolyElement newElement(Object value) {
        return new PolyElement(this, (List) value);
    }

    /**
     * computes an n = 2^k point convolution.
     * if deg(x) >= 2^k, then x is first reduced modulo X^n-1.
     **/
    void ToFFTRep(PolyElement x, long k, int lo, int hi) {
/*        Vector<Long> t = new Vector<>();

        if (k > fftInfo.getMaxRoot())
            throw new IllegalArgumentException("Polynomial too big for FFT");

        if (lo < 0)
            throw new IllegalArgumentException("bad arg to ToFFTRep");

        t.setSize(fftInfo.getNumPrimes());

        hi = Math.min(hi, x.getDegree());

        FFTRepresentation representation = new FFTRepresentation(fftInfo, k);

        int n = 1 << k;
        int m = Math.max(hi-lo + 1, 0);

        for (int j = 0; j < n; j++) {
            if (j >= m) {
                for (int i = 0; i < fftInfo.getNumPrimes(); i++)
                    representation.setAttbl[i][j] = 0;
            }
            else {
                Element accum = x.getAt(j+lo).duplicate();
                for (int j1 = j + n; j1 < m; j1 += n)
                    add(accum, accum, x.getAt(j1+lo));

                ToModularRep(t, accum, FFTInfo, TmpSpace);

                for (int i = 0; i < fftInfo.getNumPrimes(); i++) {
                    y.tbl[i][j] = t[i];
                }
            }
        }

        // FIXME: something to think about...part of the above logic
        // is essentially a matrix transpose, which could lead to bad
        // cache performance.  I don't really know if that is an issue.

        for (i = 0; i < FFTInfo->NumPrimes; i++) {
            long *yp = &y.tbl[i][0];
            FFTFwd(yp, yp, k, i);
        }
        */
    }


}
