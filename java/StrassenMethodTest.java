import java.io.*;
import java.util.Scanner;

/**
 * Created by lcx on 2017/3/28.
 */
public class StrassenMethodTest {

    private StrassenMethod strassenMultiply;

    StrassenMethodTest() {
        strassenMultiply = new StrassenMethod();
    }//end cons

    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("input.txt")));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("output.txt")));
        int arow = Integer.parseInt(bufferedReader.readLine());
        int acol = arow;
        int brow = arow;
        int bcol = arow;
        double[][] A = new double[arow][arow];
        double[][] B = new double[arow][arow];
        double[][] C = new double[arow][bcol];
        for (int r = 0; r < arow; r++) {
            String[] num = bufferedReader.readLine().split(",");

            for (int c = 0; c < arow; c++) {
                A[r][c] = Float.parseFloat(num[c]);
            }//end inner loop
        }//en
        bufferedReader.readLine();
        for (int r = 0; r < arow; r++) {
            String[] num = bufferedReader.readLine().split(",");
            for (int c = 0; c < arow; c++) {
                B[r][c] = Float.parseFloat(num[c]);
            }//end inner loop
        }//end loop


        System.out.println("矩阵的大小是" + arow);

        StrassenMethodTest algorithm = new StrassenMethodTest();
        C = algorithm.multiplyRectMatrix(A, B, arow, acol, brow, bcol);

        //Display the calculation result:
        System.out.println("结果输出在output.txt ");
        for (int r = 0; r < arow; r++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int c = 0; c < bcol; c++) {
                stringBuilder.append(C[r][c]);
                stringBuilder.append("  ");
            }//end inner loop
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.newLine();
        }//end outter loop
        bufferedWriter.flush();
        bufferedReader.close();
        bufferedReader.close();
    }//end main


    //Deal with matrices that are not square:
    public double[][] multiplyRectMatrix(double[][] A, double[][] B,
                                         int arow, int acol, int brow, int bcol) {
        if (arow != bcol) //Invalid multiplicatio
            return new double[][]{{0}};

        double[][] C = new double[arow][bcol];

        if (arow < acol) {

            double[][] newA = new double[acol][acol];
            double[][] newB = new double[brow][brow];

            int n = acol;

            for (int r = 0; r < acol; r++)
                for (int c = 0; c < acol; c++)
                    newA[r][c] = 0.0;

            for (int r = 0; r < brow; r++)
                for (int c = 0; c < brow; c++)
                    newB[r][c] = 0.0;

            for (int r = 0; r < arow; r++)
                for (int c = 0; c < acol; c++)
                    newA[r][c] = A[r][c];

            for (int r = 0; r < brow; r++)
                for (int c = 0; c < bcol; c++)
                    newB[r][c] = B[r][c];

            double[][] C2 = multiplySquareMatrix(newA, newB, n);
            for (int r = 0; r < arow; r++)
                for (int c = 0; c < bcol; c++)
                    C[r][c] = C2[r][c];
        }//end if

        else if (arow == acol)
            C = multiplySquareMatrix(A, B, arow);

        else {
            int n = arow;
            double[][] newA = new double[arow][arow];
            double[][] newB = new double[bcol][bcol];

            for (int r = 0; r < arow; r++)
                for (int c = 0; c < arow; c++)
                    newA[r][c] = 0.0;

            for (int r = 0; r < bcol; r++)
                for (int c = 0; c < bcol; c++)
                    newB[r][c] = 0.0;


            for (int r = 0; r < arow; r++)
                for (int c = 0; c < acol; c++)
                    newA[r][c] = A[r][c];

            for (int r = 0; r < brow; r++)
                for (int c = 0; c < bcol; c++)
                    newB[r][c] = B[r][c];

            double[][] C2 = multiplySquareMatrix(newA, newB, n);
            for (int r = 0; r < arow; r++)
                for (int c = 0; c < bcol; c++)
                    C[r][c] = C2[r][c];
        }//end else

        return C;
    }//end method

    //Deal with matrices that are square matrices.
    public double[][] multiplySquareMatrix(double[][] A2, double[][] B2, int n) {

        double[][] C2 = new double[n][n];

        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                C2[r][c] = 0;

        if (n == 1) {
            C2[0][0] = A2[0][0] * B2[0][0];
            return C2;
        }//end if

        int exp2k = 2;

        while (exp2k <= (n / 2)) {
            exp2k *= 2;
        }//end loop

        if (exp2k == n) {
            C2 = strassenMultiply.strassenMultiplyMatrix(A2, B2, n);
            return C2;
        }//end else

        //The "biggest" strassen matrix:
        double[][][] A = new double[6][exp2k][exp2k];
        double[][][] B = new double[6][exp2k][exp2k];
        double[][][] C = new double[6][exp2k][exp2k];

        for (int r = 0; r < exp2k; r++) {
            for (int c = 0; c < exp2k; c++) {
                A[0][r][c] = A2[r][c];
                B[0][r][c] = B2[r][c];
            }//end inner loop
        }//end outter loop

        C[0] = strassenMultiply.strassenMultiplyMatrix(A[0], B[0], exp2k);

        for (int r = 0; r < exp2k; r++)
            for (int c = 0; c < exp2k; c++)
                C2[r][c] = C[0][r][c];

        int middle = exp2k / 2;

        for (int r = 0; r < middle; r++) {
            for (int c = exp2k; c < n; c++) {
                A[1][r][c - exp2k] = A2[r][c];
                B[3][r][c - exp2k] = B2[r][c];
            }//end inner loop
        }//end outter loop

        for (int r = exp2k; r < n; r++) {
            for (int c = 0; c < middle; c++) {
                A[3][r - exp2k][c] = A2[r][c];
                B[1][r - exp2k][c] = B2[r][c];
            }//end inner loop
        }//end outter loop

        for (int r = middle; r < exp2k; r++) {
            for (int c = exp2k; c < n; c++) {
                A[2][r - middle][c - exp2k] = A2[r][c];
                B[4][r - middle][c - exp2k] = B2[r][c];
            }//end inner loop
        }//end outter loop

        for (int r = exp2k; r < n; r++) {
            for (int c = middle; c < n - exp2k + 1; c++) {
                A[4][r - exp2k][c - middle] = A2[r][c];
                B[2][r - exp2k][c - middle] = B2[r][c];
            }//end inner loop
        }//end outter loop

        for (int i = 1; i <= 4; i++)
            C[i] = multiplyRectMatrix(A[i], B[i], middle, A[i].length, A[i].length, middle);

        /*
        Calculate the final results of grids in the "biggest 2^k square,
        according to the rules of matrice multiplication.
        */
        for (int row = 0; row < exp2k; row++) {
            for (int col = 0; col < exp2k; col++) {
                for (int k = exp2k; k < n; k++) {
                    C2[row][col] += A2[row][k] * B2[k][col];
                }//end loop
            }//end inner loop
        }//end outter loop

        //Use brute force to solve the rest, will be improved later:
        for (int col = exp2k; col < n; col++) {
            for (int row = 0; row < n; row++) {
                for (int k = 0; k < n; k++)
                    C2[row][col] += A2[row][k] * B2[k][row];
            }//end inner loop
        }//end outter loop

        for (int row = exp2k; row < n; row++) {
            for (int col = 0; col < exp2k; col++) {
                for (int k = 0; k < n; k++)
                    C2[row][col] += A2[row][k] * B2[k][row];
            }//end inner loop
        }//end outter loop

        return C2;
    }//end method

}//end class
