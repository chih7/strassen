/**
 * Created by lcx on 2017/3/28.
 */
public class StrassenMethod {

    private double[][][][] A = new double[2][2][][];
    private double[][][][] B = new double[2][2][][];
    private double[][][][] C = new double[2][2][][];

    /*//Codes for testing this class:
        public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Input size of the matrix: ");
        int n = input.nextInt();

        double[][] A = new double[n][n];
        double[][] B = new double[n][n];
        double[][] C = new double[n][n];
        System.out.println("Input data for matrix A: ");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                System.out.printf("Data of A[%d][%d]: ", r, c);
                A[r][c] = input.nextDouble();
            }//end inner loop
        }//end loop

        System.out.println("Input data for matrix B: ");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                System.out.printf("Data of A[%d][%d]: ", r, c);
                B[r][c] = input.nextDouble();
            }//end inner loop
        }//end loop

        StrassenMethod algorithm = new StrassenMethod();
        C = algorithm.strassenMultiplyMatrix(A, B, n);

        System.out.println("Result from matrix C: ");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                System.out.printf("Data of C[%d][%d]: %f\n", r, c, C[r][c]);
            }//end inner loop
        }//end outter loop

    }//end main*/

    public double[][] strassenMultiplyMatrix(double[][] A2, double B2[][], int n){
        double[][] C2 = new double[n][n];
        //Initialize the matrix:
        for(int rowIndex = 0; rowIndex < n; rowIndex++)
            for(int colIndex = 0; colIndex < n; colIndex++)
                C2[rowIndex][colIndex] = 0.0;

        if(n == 1)
            C2[0][0] = A2[0][0] * B2[0][0];
            //"Slice matrices into 2 * 2 parts:
        else{
            double[][][][] A = new double[2][2][n / 2][n / 2];
            double[][][][] B = new double[2][2][n / 2][n / 2];
            double[][][][] C = new double[2][2][n / 2][n / 2];

            for(int r = 0; r < n / 2; r++){
                for(int c = 0; c < n / 2; c++){
                    A[0][0][r][c] = A2[r][c];
                    A[0][1][r][c] = A2[r][n / 2 + c];
                    A[1][0][r][c] = A2[n / 2 + r][c];
                    A[1][1][r][c] = A2[n / 2 + r][n / 2 + c];

                    B[0][0][r][c] = B2[r][c];
                    B[0][1][r][c] = B2[r][n / 2 + c];
                    B[1][0][r][c] = B2[n / 2 + r][c];
                    B[1][1][r][c] = B2[n / 2 + r][n / 2 + c];
                }//end loop
            }//end loop

            n = n / 2;

            double[][][] S = new double[10][n][n];
            S[0] = minusMatrix(B[0][1], B[1][1], n);
            S[1] = addMatrix(A[0][0], A[0][1], n);
            S[2] = addMatrix(A[1][0], A[1][1], n);
            S[3] = minusMatrix(B[1][0], B[0][0], n);
            S[4] = addMatrix(A[0][0], A[1][1], n);
            S[5] = addMatrix(B[0][0], B[1][1], n);
            S[6] = minusMatrix(A[0][1], A[1][1], n);
            S[7] = addMatrix(B[1][0], B[1][1], n);
            S[8] = minusMatrix(A[0][0], A[1][0], n);
            S[9] = addMatrix(B[0][0], B[0][1], n);

            double[][][] P = new double[7][n][n];
            P[0] = strassenMultiplyMatrix(A[0][0], S[0], n);
            P[1] = strassenMultiplyMatrix(S[1], B[1][1], n);
            P[2] = strassenMultiplyMatrix(S[2], B[0][0], n);
            P[3] = strassenMultiplyMatrix(A[1][1], S[3], n);
            P[4] = strassenMultiplyMatrix(S[4], S[5], n);
            P[5] = strassenMultiplyMatrix(S[6], S[7], n);
            P[6] = strassenMultiplyMatrix(S[8], S[9], n);

            C[0][0] = addMatrix(minusMatrix(addMatrix(P[4], P[3], n), P[1], n), P[5], n);
            C[0][1] = addMatrix(P[0], P[1], n);
            C[1][0] = addMatrix(P[2], P[3], n);
            C[1][1] = minusMatrix(minusMatrix(addMatrix(P[4], P[0], n), P[2], n), P[6], n);

            n *= 2;

            for(int r = 0; r < n / 2; r++){
                for(int c = 0; c < n / 2; c++){
                    C2[r][c] = C[0][0][r][c];
                    C2[r][n / 2 + c] = C[0][1][r][c];
                    C2[n / 2 + r][c] = C[1][0][r][c];
                    C2[n / 2 + r][n / 2 + c] = C[1][1][r][c];
                }//end inner loop
            }//end outter loop
        }//end else

        return C2;
    }//end method

    //Add two matrices according to matrix addition.
    private double[][] addMatrix(double[][] A, double[][] B, int n){
        double C[][] = new double[n][n];

        for(int r = 0; r < n; r++)
            for(int c = 0; c < n; c++)
                C[r][c] = A[r][c] + B[r][c];

        return C;
    }//end method

    //Substract two matrices according to matrix addition.
    private double[][] minusMatrix(double[][] A, double[][] B, int n){
        double C[][] = new double[n][n];

        for(int r = 0; r < n; r++)
            for(int c = 0; c < n; c++)
                C[r][c] = A[r][c] - B[r][c];

        return C;
    }//end method

}//end class
