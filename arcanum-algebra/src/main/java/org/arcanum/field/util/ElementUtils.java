package org.arcanum.field.util;

import org.arcanum.Element;
import org.arcanum.ElementPow;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.common.parameters.Parameters;
import org.arcanum.field.vector.ListVectorElement;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.VectorField;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ElementUtils {

    public static Element[] duplicate(Element[] source) {
        Element[] target = new Element[source.length];
        for (int i = 0; i < target.length; i++)
            target[i] = source[i].duplicate();

        return target;
    }

    public static Element[] cloneImmutable(Element[] source) {
        if (source == null)
            return null;

        Element[] target = Arrays.copyOf(source, source.length);

        for (int i = 0; i < target.length; i++) {
            Element uElement = target[i];
            if (uElement != null && !uElement.isImmutable())
                target[i] = target[i].getImmutable();
        }

        return target;
    }

    public static <K> Map<K, Element[]> cloneImmutable(Map<K, Element[]> source) {
        Map<K, Element[]> dest = new HashMap<K, Element[]>(source.size());

        for (Map.Entry<K, Element[]> kEntry : source.entrySet())
            dest.put(kEntry.getKey(), cloneImmutable(kEntry.getValue()));

        return dest;
    }

    public static <K> Map<K, Element> cloneImmutable2(Map<K, Element> source) {
        Map<K, Element> dest = new HashMap<K, Element>(source.size());

        for (Map.Entry<K, Element> kEntry : source.entrySet())
            dest.put(kEntry.getKey(), kEntry.getValue().getImmutable());

        return dest;
    }

    public static ElementPow[] cloneToElementPow(Element[] source) {
        ElementPow[] target = new ElementPow[source.length];

        for (int i = 0; i < target.length; i++) {
            target[i] = source[i].getElementPowPreProcessing();
        }

        return target;
    }

    public static Element randomIn(Field field, Element generator) {
        return generator.duplicate().powZn(field.newRandomElement());
    }

    public static Element getGenerator(Element generator, Parameters parameters, int subgroupIndex, int numPrimes) {
        BigInteger prod = BigInteger.ONE;
        for (int j = 0; j < numPrimes; j++) {
            if (j != subgroupIndex)
                prod = prod.multiply(parameters.getBigIntegerAt("n", j));
        }

        return generator.pow(prod);
    }

    public static void print(Element[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(matrix[i][j] + ", ");

            }
            System.out.println();
        }
        System.out.println();
    }


    public static Element[][] transpose(Element[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {

            for (int j = i + 1; j < n; j++) {

                Element temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        return matrix;
    }

    public static Element[][] multiply(Element[][] a, Element[][] b) {
        int n = a.length;
        Field field = a[0][0].getField();

        Element[][] res = new Element[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                res[i][j] = field.newZeroElement();
                for (int k = 0; k < n; k++)
                    res[i][j].add(a[i][k].duplicate().mul(b[k][j]));
            }
        }

        return res;
    }

    public static void copyArray(Element[][] target, Element[][] source, int sizeY, int sizeX, int y, int x) {
        for (int i = y; i < sizeY; i++) {
            for (int j = x; j < sizeX; j++) {
                target[i - y][j - x] = source[i][j].duplicate();
            }
        }
    }

    public static ListVectorElement union(Element a, Element b) {
        ListVectorElement va = (ListVectorElement) a;
        ListVectorElement vb = (ListVectorElement) b;

        VectorField f = new VectorField<Field>(
                va.getField().getRandom(),
                va.getField().getTargetField(),
                va.getSize() + vb.getSize()
        );
        ListVectorElement r = f.newElement();
        int counter = 0;

        for (int i = 0; i < va.getSize(); i++)
            r.getAt(counter++).set(va.getAt(i));

        for (int i = 0; i < vb.getSize(); i++)
            r.getAt(counter++).set(vb.getAt(i));

        return r;
    }

    public static Element bd(Element a, int k) {
        Matrix A = (Matrix) a;
        Matrix R = new MatrixField<Field>(null, A.getTargetField(), A.getN() * k, A.getM()).newElement();

        for (int i = 0, n = A.getN(); i < n; i++) {

            for (int j = 0, m = A.getM(); j < m; j++) {
                String value = A.getAt(i, j).toCanonicalBigInteger().toString(2);

                for (int s = 0, t = value.length() - 1; s < k; s++, t--) {
                    if (t < 0)
                        R.setZeroAt((i * k) + s, j);
                    else if (value.charAt(t) == '1')
                        R.setOneAt((i * k) + s, j);
                    else
                        R.setZeroAt((i * k) + s, j);
                }
            }
        }

        return R;
    }

    public static Element newDiagonalPrimitiveMatrix(SecureRandom random, Field targetField, int n, int k) {
        return new MatrixField<Field>(random, targetField, n, n * k).newElementIdentity(
                new VectorField<Field>(random, targetField, k).newPrimitiveElement()
        );
    }

    public static void assertTrue(boolean value) {
        if (!value)
            throw new IllegalStateException("Expected true, was false!");
    }

}
