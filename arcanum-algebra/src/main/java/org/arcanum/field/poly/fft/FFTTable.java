package org.arcanum.field.poly.fft;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class FFTTable {

    private static FFTTable instance;
    static {
        instance = new FFTTable();
    }


    public static FFTTable getInstance() {
        return instance;
    }


    private List<FFTPrime> fftPrimes;
    private FFTPrimeGenerator fftPrimeGenerator;


    private FFTTable() {
        this.fftPrimes = new ArrayList<>();
        this.fftPrimeGenerator = new FFTPrimeGenerator();
    }

    public int size() {
        return fftPrimes.size();
    }

    public FFTPrime getAt(int index) {
        if (index >= fftPrimes.size()) {
            // fill the list of primes
            for (int i = 0, diff = index - fftPrimes.size() + 1; i < diff; i++)
                fftPrimes.add(fftPrimeGenerator.next());
        }

        return fftPrimes.get(index);
    }

}
