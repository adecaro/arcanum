package org.arcanum.util.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class IOUtils {

    public static InputStream openInputStream(String path) {
        InputStream inputStream;

        File file = new File(path);
        if (file.exists()) {
            try {
                inputStream = file.toURI().toURL().openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        }

        if (inputStream == null)
            throw new IllegalArgumentException("No valid resource found!");

        return inputStream;
    }

}
