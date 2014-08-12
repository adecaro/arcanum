package org.arcanum.pairing.mm.clt13.parameters;

import org.arcanum.common.parameters.Parameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class CTL13MMPublicParameters {
    private Parameters parameters;


    public CTL13MMPublicParameters(Parameters parameters) {
        this.parameters = parameters;
    }


    public CTL13MMSystemParameters getSystemParameters() {
        return (CTL13MMSystemParameters) parameters.getObject("params");
    }

    public BigInteger getX0() {
        return parameters.getBigInteger("x0");
    }

    public BigInteger getY() {
        return parameters.getBigInteger("y");
    }

    public BigInteger getZ() {
        return parameters.getBigInteger("z");
    }

    public BigInteger getZInv() {
        return parameters.getBigInteger("zInv");
    }

    public BigInteger getPzt() {
        return parameters.getBigInteger("pzt");
    }

    public BigInteger getXspAt(int index) {
        return parameters.getBigIntegerAt("xsp", index);
    }

    public BigInteger getCrtCoefficientAt(int index) {
        return parameters.getBigIntegerAt("crtCoefficients", index);
    }

    public BigInteger getXsAt(int level, int index) {
        return parameters.getBigIntegerAt("xs" + level, index);
    }

    public BigInteger getGsAt(int index) {
        return parameters.getBigIntegerAt("gs", index);
    }

    public BigInteger getPsAt(int index) {
        return parameters.getBigIntegerAt("ps", index);
    }

    public BigInteger getZInvPowAt(int index) {
        return parameters.getBigIntegerAt("zInvPow", index);
    }

    public BigInteger getYPowAt(int index) {
        return parameters.getBigIntegerAt("yPow", index);
    }
}
