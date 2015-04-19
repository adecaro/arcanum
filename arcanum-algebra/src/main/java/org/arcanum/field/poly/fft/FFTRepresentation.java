package org.arcanum.field.poly.fft;

import static org.arcanum.field.poly.fft.FFTUtils.NTL_BITS_PER_LONG;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class FFTRepresentation {

    private FFTInfo fftInfo;
    
    private long k;                // a 2^k point representation
    private long MaxK;             // maximum space allocated
    private long[][] tbl;
    private long numPrimes;

    public FFTRepresentation(FFTInfo fftInfo) {
        this.k = this.MaxK = -1;
        this.tbl = null;
        this.numPrimes = 0;
        this.fftInfo = fftInfo;
    }

    public FFTRepresentation(FFTInfo fftInfo, int size) {
        this.k = this.MaxK = -1;
        this.tbl = null;
        this.numPrimes = 0;
        this.fftInfo = fftInfo;

        setSize(size);
    }

    public void setSize(long NewK) {
        if (NewK < -1 || NewK >= NTL_BITS_PER_LONG-1)
            throw new IllegalArgumentException("bad arg to FFTRep::SetSize()");

        if (NewK <= MaxK) {
            k = NewK;
            return;
        }

        if (MaxK == -1)
            numPrimes = fftInfo.getNumPrimes();
        else {
            if (numPrimes != fftInfo.getNumPrimes())
                throw new IllegalStateException("FFTRep: inconsistent use");
        }

        this.tbl = new long[(int) fftInfo.getNumPrimes()][1 << NewK];
        this.k = this.MaxK = NewK;
    }

}
