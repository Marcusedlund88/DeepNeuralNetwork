package com.example.neuralnetwork.Math;

import java.io.Serializable;
import java.util.Random;

public interface MathOperations{

        double[] fillVectorWithSameValue(int columns, int value);

        double[][] fillMatrixWithSameValue(int rows, int columns, int value);

        double generateRandomBias(int edgesIn, int edgesOut);

        double updateBias(double learnRate, double bias, double[] dC_dB);

        double[] vectorMatrixMultiplication(double[] vector, double[][] matrix);

        double[] vectorScalarMultiplication(double scalar, double[] vector);

        double[] elementWiseVectorMultiplication(double[] vector1, double[] vector2);

        double[] makeZeroVector(int length);

        double[][] makeZeroMatrix(int rows, int columns);

        double[][] AddMatrix(double[][] matrixOne, double[][] matrixTwo);

        double[][] GetPrediction(double[][] input);

        double[][] transposeMatrix(double[][] inputMatrix);

        double GetPrediction(double[] input);

        double[] weightedSum(double[][] input, double[][] weights);

        double[] ReluActivateNeuron(double[] weightedInput);

        double[] outputSigmoidActivation(double[] vector);

        double[] outputSoftMaxActivation(double[] vector);

        double[][] dC_dA(double[][] predictedValue, double[][] expectedValue);

        double[] vectorAddition(double[] vector1, double[] vector2);
}
