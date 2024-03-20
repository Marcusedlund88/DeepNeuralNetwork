package com.example.neuralnetwork.Math;

import static org.junit.jupiter.api.Assertions.*;

class StaticMathClassTest {

    @org.junit.jupiter.api.Test
    void fillVectorWithSameValue() {
    }

    @org.junit.jupiter.api.Test
    void fillMatrixWithSameValue() {
    }

    @org.junit.jupiter.api.Test
    void generateRandomBias() {
    }

    @org.junit.jupiter.api.Test
    void updateBias() {
    }

    @org.junit.jupiter.api.Test
    void vectorMatrixMultiplication() {
        double[][] matrix = {{1,2,3},{1,2,3},{1,2,3}};
        double[] vector = {1,2,3};

        double[] output = StaticMathClass.vectorMatrixMultiplication(vector, matrix);
        double[] actual = {14,14,14};
        assertArrayEquals(output,actual);

    }

    @org.junit.jupiter.api.Test
    void vectorScalarMultiplication() {
    }

    @org.junit.jupiter.api.Test
    void vectorMultiplication() {
        double[] vector1 = {1,2,3};
        double[] vector2 = {4,5,6};

        double[] output = StaticMathClass.elementWiseVectorMultiplication(vector1,vector2);
        double[] actual = {};

        assertArrayEquals(output,actual);
    }

    @org.junit.jupiter.api.Test
    void makeZeroVector() {
    }

    @org.junit.jupiter.api.Test
    void makeZeroMatrix() {
    }

    @org.junit.jupiter.api.Test
    void addMatrix() {
    }

    @org.junit.jupiter.api.Test
    void getPrediction() {
    }

    @org.junit.jupiter.api.Test
    void transposeMatrix() {
    }

    @org.junit.jupiter.api.Test
    void testGetPrediction() {
    }

    @org.junit.jupiter.api.Test
    void dC_dW_Output() {
    }

    @org.junit.jupiter.api.Test
    void dC_dW_hidden() {
    }

    @org.junit.jupiter.api.Test
    void weightedSum() {
    }

    @org.junit.jupiter.api.Test
    void reluActivateNeuron() {
    }

    @org.junit.jupiter.api.Test
    void generateStartingWeights() {
    }

    @org.junit.jupiter.api.Test
    void outputSigmoidActivation() {
    }

    @org.junit.jupiter.api.Test
    void dC_dA() {
    }

    @org.junit.jupiter.api.Test
    void dA_dZ_sigmoid() {
    }

    @org.junit.jupiter.api.Test
    void dA_dZ_relu() {
    }

    @org.junit.jupiter.api.Test
    void getUpdatedWeights() {
    }

    @org.junit.jupiter.api.Test
    void vectorAddition() {
        double[] vector1 = {1,2,3};
        double[] vector2 = {4,5,6};

        double[] output = StaticMathClass.vectorAddition(vector1,vector2);
        double[] actual = {5,7,9};

        assertArrayEquals(output,actual);
    }

    @org.junit.jupiter.api.Test
    void vectorTrainingBigFiveCompability() {
    }

    @org.junit.jupiter.api.Test
    void createRandomTrainingInput() {
    }

    @org.junit.jupiter.api.Test
    void normalizeInput() {
    }
}