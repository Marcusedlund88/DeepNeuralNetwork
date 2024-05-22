package com.example.neuralnetwork.Math;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class StaticMathOperationsTest {

    private static MathOperations mathOperation;

    @BeforeAll
    static void setUp() {
        mathOperation = new StaticMathOperations();
    }

    @org.junit.jupiter.api.Test
    void fillVectorWithSameValue() {
        int size = 5;
        int value = 1;
        double[] newVector = mathOperation.fillVectorWithSameValue(5,1);

        for(int i = 0; i < newVector.length; i++){
            assertEquals(1, newVector[i]);
        }
        assertEquals(5, newVector.length);
    }

    @org.junit.jupiter.api.Test
    void fillMatrixWithSameValue() {

        int rows = 5;
        int columns = 5;
        int value = 1;

        double[][] matrix = mathOperation.fillMatrixWithSameValue(rows, columns, value);

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                assertEquals(1, matrix[i][j]);
            }
        }
        assertEquals(5, matrix.length);
        assertEquals(5, matrix[0].length);
    }

    @org.junit.jupiter.api.Test
    void vectorMatrixMultiplication() {

        double[] vector = {2,2,2};
        double[][] matrix = {{3,3,3},{3,3,3},{3,3,3}};

        double[] newVector = mathOperation.vectorMatrixMultiplication(vector, matrix);
        for(int i = 0; i < newVector.length; i++){
            assertEquals(18, newVector[i]);
        }
        assertEquals(3,newVector.length);

    }

    @org.junit.jupiter.api.Test
    void vectorScalarMultiplication() {
        double[] vector = {2,2,2};
        int scalar = 5;

        double[] newVector = mathOperation.vectorScalarMultiplication(scalar, vector);

        for(int i = 0; i < newVector.length; i++){
            assertEquals(10, newVector[i]);
        }
        assertEquals(3, newVector.length);
    }

    @org.junit.jupiter.api.Test
    void makeZeroVector() {
        double[] newVector = mathOperation.makeZeroVector(5);
        for(int i = 0; i < newVector.length; i++){
            assertEquals(0, newVector[i]);
        }

        assertEquals(5, newVector.length);
    }

    @org.junit.jupiter.api.Test
    void makeZeroMatrix() {
        double[][] matrix = mathOperation.makeZeroMatrix(5,5);
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                assertEquals(0, matrix[i][j]);
            }
        }
        assertEquals(5, matrix.length);
        assertEquals(5, matrix[0].length);
    }

    @org.junit.jupiter.api.Test
    void transposeMatrix() {
        double[][] matrix = {{1,1,1},{2,2,2},{3,3,3}};
        double[][] transposedMatrix = mathOperation.transposeMatrix(matrix);

        assertEquals(1, transposedMatrix[0][0]);
        assertEquals(2, transposedMatrix[0][1]);
        assertEquals(3, transposedMatrix[0][2]);
    }
}