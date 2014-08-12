package org.arcanum.common.math;

import org.arcanum.Element;
import org.arcanum.Matrix;

public class Cholesky {

    // return Cholesky factor L of psd matrix A = L L^T
    public static Matrix cholesky(Matrix A) {
        if (!A.isSquare())
            throw new RuntimeException("Matrix is not square");
        if (!A.isSymmetric())
            throw new RuntimeException("Matrix is not symmetric");

        int N = A.getN();
        Matrix L = (Matrix) A.getField().newElement();
        Element sum = A.getTargetField().newElement();
        Element zero = A.getTargetField().newZeroElement();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= i; j++) {
                sum.setToZero();
                for (int k = 0; k < j; k++) {
                    sum.add(L.getAt(i, k).duplicate().mul(L.getAt(j, k)));
                }

                if (i == j)
                    L.getAt(i, i).set(A.getAt(i, i).duplicate().sub(sum)).sqrt();
                else
                    L.getAt(i, j).set(
                            A.getTargetField().newOneElement().div(L.getAt(j, j)).mul(A.getAt(i, j).duplicate().sub(sum))
                    );
            }
            if (L.getAt(i, i).compareTo(zero) <= 0) {
                throw new RuntimeException("Matrix not positive definite");
            }
        }

        return L;
    }

    public static Matrix cholesky2(Matrix L) {
        if (!L.isSquare())
            throw new RuntimeException("Matrix is not square");
        if (!L.isSymmetric())
            throw new RuntimeException("Matrix is not symmetric");

        int N = L.getN();
        for (int k = 0; k < N; k++) {
            System.out.println("L.getAt(k,k) = " + L.getAt(k, k));
            L.getAt(k, k).sqrt();

            for (int i = k + 1; i < N; i++) {
                L.getAt(i, k).div(L.getAt(k, k));
            }

            for (int j = k + 1; j < N; j++) {
                for (int i = j; i < N; i++) {
                    L.getAt(i, j).sub(
                            L.getAt(i, k).duplicate().mul(L.getAt(j, k))
                    );
                }

            }

        }

        return L;
    }

    public static Matrix choleskyAt(Matrix L, int offsetRow, int offsetCol) {
        int N = L.getN() - offsetRow;

        for (int k = 0; k < N; k++) {
//            System.out.println("L.getAt(k,k) = " + L.getAt(offsetRow + k, offsetCol + k));
            L.getAt(offsetRow + k, offsetCol + k).sqrt();

            for (int i = k + 1; i < N; i++) {
                L.getAt(offsetRow + i, offsetCol + k).div(L.getAt(offsetRow + k, offsetCol + k));
            }

            for (int j = k + 1; j < N; j++) {
                for (int i = j; i < N; i++) {
                    L.getAt(offsetRow + i, offsetCol + j).sub(
                            L.getAt(offsetRow + i, offsetCol + k).duplicate().mul(L.getAt(offsetRow + j, offsetCol + k))
                    );
                }
            }
        }

        // cleanup
        for (int i = offsetRow; i < offsetRow + N; i++) {
            for (int j = offsetCol; j < offsetCol + N; j++) {
                if (j > i)
                    L.getAt(i, j).setToZero();
            }
        }

        return L;
    }


    // is symmetric
    public static boolean isSymmetric(double[][] A) {
        int N = A.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                if (A[i][j] != A[j][i]) return false;
            }
        }
        return true;
    }

    // is symmetric
    public static boolean isSquare(double[][] A) {
        int N = A.length;
        for (int i = 0; i < N; i++) {
            if (A[i].length != N) return false;
        }
        return true;
    }


    // return Cholesky factor L of psd matrix A = L L^T
    public static double[][] cholesky(double[][] A) {
        if (!isSquare(A)) {
            throw new RuntimeException("Matrix is not square");
        }
        if (!isSymmetric(A)) {
            throw new RuntimeException("Matrix is not symmetric");
        }

        int N = A.length;
        double[][] L = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0.0;
                for (int k = 0; k < j; k++) {
                    sum += L[i][k] * L[j][k];
                }
                if (i == j)
                    L[i][i] = Math.sqrt(A[i][i] - sum);
                else
                    L[i][j] = 1.0 / L[j][j] * (A[i][j] - sum);
            }
            if (L[i][i] <= 0) {
                throw new RuntimeException("Matrix not positive definite");
            }
        }
        return L;
    }


    // sample client
    public static void main(String[] args) {
        int N = 3;
        double[][] A = {
                {10, 0, 1},
                {0, 5, 3},
                {1, 3, 15}
        };
        double[][] L = cholesky(A);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("%8.5f ", L[i][j]);
            }
            System.out.println();
        }

    }

}