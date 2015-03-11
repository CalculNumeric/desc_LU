package com.calcul_numeric;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    // system dimension
    static Integer n;

    // epsilon - precision
    static Integer epsilon;

    // matrices
    static Double [][] A ;
    static Double [][] Ainit;

    // free terms vector
    static Double [] b;

    // solutions for the ecuations
    static ArrayList<Double> X = new ArrayList<Double>();


    public static void main(String[] args) throws FileNotFoundException {

        readFromFile("files/input.txt");
        displayMatrix(A);

        splittingMatrix();
        displayMatrix(A);

        System.out.println("det(L)*det(U): " + computeDeterminant(A) + "\n");

        computeDirectSubstitution();
        computeReverseSubstitution();

        System.out.println("\nVerify: " + verify());
    }

    /**
     * Method that verify our solution by computin euclidian norm of the error of the  vector Ainit * X - b
     * @return
     */
    private static Boolean verify() {
        Boolean result = false;
        Double error = 0.0;

        for (int i = 0; i < n; i++) {

            Double aux = 0.0;
            for (int j = 0; j < n; j++) {
                aux += Ainit[i][j]*X.get(j);
            }

            error += Math.pow(aux - b[i], 2.0);
        }

        error = Math.sqrt(error);

        if (error < Math.pow(10, (-1)*epsilon)) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    /**
     * Method that implements the "reverse substitution method" on upper triangular matrix,
     * and alongside "direct substitution method",
     * solves the equation
     */
    private static void computeReverseSubstitution() {
        System.out.println("Metoda substituiei inverse: ");

        for (int i = n - 1; i >= 0; i--) {
            X.set(i, (X.get(i) - computeSumReverse(i)) / A[i][i]);
        }

        System.out.println(X);
    }

    /**
     * Method that calculates equations for the i-th row,
     * for reverse substition method
     * @param i - row
     * @return sum (double)
     */
    private static Double computeSumReverse(int i) {
        Double result = 0.0;

        for (int j = i + 1; j < n; j++) {
            result += A[i][j] * X.get(j);
        }

        return result;
    }

    /**
     * Method that implements the "direct substitution method" on lower triangular matrix
     */
    private static void computeDirectSubstitution() {
        System.out.println("Metoda substituiei directe: ");

        for (int i = 0; i < n; i++) {
            X.add(i, b[i] - computeSumDirect(i));
        }

        System.out.println(X);
    }

    /**
     * Method that computes equations for the i-th row,
     * for direct substitution method
     * @param i - row
     * @return double
     */
    private static Double computeSumDirect(int i) {
        Double result = 0.0;

        for (int j = 0; j < i; j++) {
            result += A[i][j] * X.get(j);
        }

        return result;
    }

    /**
     * Method that calculate the determinant of a triangular matrix
     * @param matrix - triangular matrix
     * @return the determinant
     */
    private static Double computeDeterminant(Double [][] matrix) {

        Double result = 1.0;

        for (int i = 0; i < n; i++) {
            result *= matrix[i][i];
        }

        return result;
    }

    /**
     * Method that splits the matrix into upper and lower triangular matrices
     */
    private static void splittingMatrix() {
        for (int i = 1; i < n; i++) {
            computeRow(i);
        }
    }

    /**
     * Method that computes elements from a specific row,
     * for the upper matrix
     * @param row
     */
    private static void computeRow(Integer row) {

        if (A[row][row] < Math.pow(10.0, (-1.0)*epsilon)) {
            return;
        }

        for (int j = 0; j < n; j++) {
            if (j < row ) {
                A[row][j] = (A[row][j] - computeSum1(j, row)) / A[j][j];
            } else {
                A[row][j] -= computeSum2(row, j);
            }
        }

    }

    /**
     * Method that calculates the sum of
     * (elements from the columns of L)*(elements from the rows of U)
     * @param p
     * @param i
     * @return
     */
    private static Double computeSum2(Integer p, Integer i) {
        Double result = 0.0;

        for (int k = 0; k <= p - 1; k++) {
            result += A[p][k] * A[k][i];
        }

        return result;
    }

    /**
     * Method that calculates the sum of
     * (elements from columns from L)*(elements from rows from U)
     * @param p - column
     * @param i - row
     * @return
     */
    private static Double computeSum1(Integer p, Integer i) {
        Double result = 0.0;

        for (int k = 0; k <= p - 1; k++) {
            if (Objects.equals(i, p)) {
                result += A[k][p];
            } else {
                result += A[i][k] * A[k][p];
            }
        }

        return result;
    }

    /**
     * Method that read matrix, epsilon and system dimension from input file
     * @param fileName
     */
    public static void readFromFile(String fileName) throws FileNotFoundException {


        Scanner input = new Scanner(new File(fileName));

        n = input.nextInt();
        epsilon = input.nextInt();

        A = new Double[n][n];
        Ainit = new Double[n][n];
        b = new Double[n];

        // matrix elements
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(input.hasNextDouble()) {
                    A[i][j] = input.nextDouble();
                    Ainit[i][j] = A[i][j];
                }
            }
        }

        // free elements
        for (int i = 0; i < n; i++) {
            if (input.hasNextDouble()) {
                b[i] = input.nextDouble();
            }
        }

    }

    /**
     * Method that displays on console the matrix components
     * @param matrix
     */
    public static void displayMatrix(Double [][] matrix) {

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                System.out.print(Math.floor(matrix[i][j] * 100) / 100 + " ");
            }
            System.out.println("");
        }

        System.out.println("");
    }
}
