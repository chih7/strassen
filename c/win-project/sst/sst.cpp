// sst.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <malloc.h>
#include <time.h>
#include <string.h>
using namespace std;
typedef struct {
    double ** arr;
    int n;
} Matrix;

static Matrix matrix_create(int n) {
    Matrix m;
    m.n = n;
    m.arr = (double**)malloc(sizeof(double*)*n);

    for(int i = 0; i < n; i++) {
        m.arr[i] = (double*)malloc(sizeof(double)*n);
        for(int j = 0; j < n; j++)
        {
            m.arr[i][j] = 0;
        }
    }
    return m;
}

static void matrix_rand(Matrix m) {
    for(int i = 0; i < m.n; i++) {
        for(int j = 0; j < m.n; j++)
        {
            m.arr[i][j] = rand()%100;
        }
    }
}

static Matrix matrix_multi(Matrix a, Matrix b) {
    if(a.n != b.n) {
        printf("error\n");
        exit(0);
    }
    Matrix m = matrix_create(a.n);
    for(int i = 0; i < a.n; i++) {
        for(int j = 0; j < a.n; j++)
        {
            for(int k = 0; k < a.n; k++)
                m.arr[i][j] += a.arr[i][k] * b.arr[k][j];
        }
    }
    return m;
}

static Matrix matrix_add(Matrix a, Matrix b) {
    if(a.n != b.n) {
        printf("error\n");
        exit(0);
    }
    Matrix m = matrix_create(a.n);
    for (int i = 0; i < a.n; i++) {
        for(int j = 0; j < a.n; j++) {
            m.arr[i][j] = a.arr[i][j] + b.arr[i][j];
        }
    }
    return m;
}

static Matrix matrix_sub(Matrix a, Matrix b) {
    if(a.n != b.n) {
        printf("error\n");
        exit(0);
    }
    Matrix m = matrix_create(a.n);
    for (int i = 0; i < a.n; i++) {
        for(int j = 0; j < a.n; j++) {
            m.arr[i][j] = a.arr[i][j] - b.arr[i][j];
        }
    }
    return m;
}

static void memory_free(Matrix m) {
    for (int i = 0; i < m.n; i++) {
        free(m.arr[i]);
    }
    free(m.arr);
}

static Matrix strassen(Matrix a, Matrix b) {
    if(a.n != b.n) {
        printf("error\n");
        exit(0);
    }
    Matrix m = matrix_create(a.n);
    if(a.n <= 8) {
        m = matrix_multi(a, b);
        return m;
    }

    int half  = a.n / 2;
    Matrix a11, a12, a21, a22, b11, b12, b21, b22;
    a11 = matrix_create(half);
    a12 = matrix_create(half);
    a21 = matrix_create(half);
    a22 = matrix_create(half);
    b11 = matrix_create(half);
    b12 = matrix_create(half);
    b21 = matrix_create(half);
    b22 = matrix_create(half);

    for(int i = 0; i < half; i++) {
        for(int j = 0; j < half; j++) {
            a11.arr[i][j] = a.arr[i][j];
            a12.arr[i][j] = a.arr[i][j+half];
            a21.arr[i][j] = a.arr[i+half][j];
            a22.arr[i][j] = a.arr[i+half][j+half];

            b11.arr[i][j] = b.arr[i][j];
            b12.arr[i][j] = b.arr[i][j+half];
            b21.arr[i][j] = b.arr[i+half][j];
            b22.arr[i][j] = b.arr[i+half][j+half];
        }
    }

    Matrix  p1, p2, p3, p4, p5, p6, p7;
    p1 = matrix_create(half);
    p2 = matrix_create(half);
    p3 = matrix_create(half);
    p4 = matrix_create(half);
    p5 = matrix_create(half);
    p6 = matrix_create(half);
    p7 = matrix_create(half);

    p1 = strassen(matrix_add(a11, a22), matrix_add(b11, b22));
    p2 = strassen(matrix_add(a21, a22), b11);
    p3 = strassen(a11, matrix_sub(b12, b22));
    p4 = strassen(a22, matrix_sub(b21, b11));
    p5 = strassen(matrix_add(a11, a12), b22);
    p6 = strassen(matrix_sub(a21, a11), matrix_add(b11, b12));
    p7 = strassen(matrix_sub(a12, a22), matrix_add(b21, b22));

    Matrix c11, c12, c21, c22;

    c11 = matrix_add(matrix_sub(matrix_add(p1, p4), p5), p7);
    c12 = matrix_add(p3, p5);
    c21 = matrix_add(p2, p4);
    c22 = matrix_add(matrix_add(matrix_sub(p1, p2), p3), p6);

    for (int i = 0; i < half; i++) {
        for(int j = 0; j < half; j++) {
            m.arr[i][j] = c11.arr[i][j];
            m.arr[i][j+half] = c12.arr[i][j];
            m.arr[i+half][j] = c21.arr[i][j];
            m.arr[i+half][j+half] = c22.arr[i][j];
        }
    }

    memory_free(a11);
    memory_free(a12);
    memory_free(a21);
    memory_free(a22);
    memory_free(b11);
    memory_free(b12);
    memory_free(b21);
    memory_free(b22);
    memory_free(p1);
    memory_free(p2);
    memory_free(p3);
    memory_free(p4);
    memory_free(p5);
    memory_free(p6);
    memory_free(p7);
    memory_free(c11);
    memory_free(c12);
    memory_free(c21);
    memory_free(c22);

    return m;
}

static void print_matrix(Matrix m) {
    for(int i = 0; i < m.n; i++) {
        for(int j = 0; j < m.n; j++)
        {
            printf("%f ", m.arr[i][j]);
        }
        printf("\n");
    }
}

int main(int argc, char** argv) {

    FILE *fp;

    fp = fopen("input.txt", "r");
    int n = 0;
    char buff[1024];
    fscanf(fp, "%d", &n);

    Matrix a = matrix_create(n);
    Matrix b = matrix_create(n);

    for(int i = 0; i < a.n; i++) {
        fscanf(fp, "%s", buff);
        char * num_str = "";
        num_str = strtok(buff, ",");
        a.arr[i][0] = atof(num_str);
        for(int j = 1; j < a.n; j++)
        {
            num_str = strtok(NULL, ",");
            a.arr[i][j] = atof(num_str);
        }
    }

    for(int i = 0; i < b.n; i++) {
        fscanf(fp, "%s", buff);
        char * num_str = "";
        num_str = strtok(buff, ",");
        b.arr[i][0] = atof(num_str);
        for(int j = 1; j < b.n; j++)
        {
            num_str = strtok(NULL, ",");
            b.arr[i][j] = atof(num_str);
        }
    }

    clock_t t = clock();
    Matrix m1 = matrix_multi(a, b);
    t = clock() - t;
    printf("original time: %f\n", ((double)t/CLOCKS_PER_SEC));

    t = clock();
    Matrix m2 = strassen(a, b);
    t = clock() - t;
    printf("strassen time: %f\n", ((double)t/CLOCKS_PER_SEC));
    print_matrix(m2);
	char jieshu;
	cin>>jieshu;
    return 1;
}

