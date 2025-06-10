package org.haldokan.edge.guava;

import com.google.common.base.Stopwatch;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Guava Hasher gives the same hash code b/w different runs of the program
 *
 * @author haldokan
 */
public class GuavaHasher {

    public static void main(String[] args) {
        HashFunction hf = Hashing.md5();
        HashCode hc = hf.newHasher().putInt(123).putDouble(2.334d).putBoolean(true)
                .putString("foobar", Charset.defaultCharset()).hash();
        System.out.println(hc);
        String s1 = "1d1679c150feaf9f3644b6e1bc5d7f6e";
        assertEquals(s1, hc.toString());

        HashCode hcode = null;
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 1000 * 1000; i++) {
            hcode = hf.newHasher().putInt(i).putDouble(2.334d).putBoolean(true)
                    .putString("foobar", Charset.defaultCharset()).putDouble(99999.343d).putDouble(99999.3431d)
                    .putDouble(99999.343d).putDouble(99999.3434d).putDouble(99999.343d).putDouble(99999.3435d)
                    .putDouble(99999.343d).putDouble(99999.3436d).putDouble(99999.343d).putDouble(99999.3437d)
                    .putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.3438d)
                    .putDouble(99999.343d).putDouble(99999.3439d).putDouble(99999.343d).putDouble(99999.34333d)
                    .putDouble(99999.343d).putDouble(99999.34344d).putDouble(99999.343d).putDouble(99999.34355d)
                    .putDouble(99999.343d).putDouble(99999.34366d).putDouble(99999.343d).putDouble(99999.34377d)
                    .putDouble(99999.343d).putDouble(99999.34388d).putDouble(99999.343d).putDouble(99999.343d)
                    .putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d)
                    .putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d)
                    .putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d)
                    .putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d)
                    .putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d)
                    .putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d)
                    .putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d)
                    .putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d).putDouble(99999.343d)
                    .putDouble(99999.343d).hash();
        }
        stopwatch.stop();
        long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(stopwatch);
        System.out.println(millis);
        System.out.println(hcode);
    }

}
