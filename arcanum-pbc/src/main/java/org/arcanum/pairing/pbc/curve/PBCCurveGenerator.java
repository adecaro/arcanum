package org.arcanum.pairing.pbc.curve;

import org.arcanum.Parameters;
import org.arcanum.ParametersGenerator;
import org.arcanum.util.parameters.PropertiesParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class PBCCurveGenerator implements ParametersGenerator {

    public Parameters generate() {
        pbcGenerate("jpbc_pbc_params.prm");

        PropertiesParameters parameters;
        try {
            parameters = new PropertiesParameters();
            File file = new File("jpbc_pbc_params.prm");
            if (!file.exists())
                throw new IllegalStateException("Failed to load parameters.");

            FileInputStream inputStream = new FileInputStream("jpbc_pbc_params.prm");
            parameters.load(inputStream);
            inputStream.close();

            if (!file.delete())
                throw new IllegalStateException("File has not been deleted");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return parameters;
    }


    protected abstract void pbcGenerate(String fileName);

}
