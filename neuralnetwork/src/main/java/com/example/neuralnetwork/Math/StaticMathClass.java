package com.example.neuralnetwork.Math;

import java.util.Random;

public class StaticMathClass {

    public static double[] vectorMatrixMultiplication(double[] vector, double[][] matrix){
        double[] temp = new double[vector.length];

        for (int i = 0; i < temp.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                temp[i] += vector[i]*matrix[i][j];
            }
        }
        return temp;
    }

    public static double[] vectorScalarMultiplication(double scalar, double[] vector){
        double outPutVector[] = new double[vector.length];
        for(int i = 0; i < outPutVector.length; i++){
            outPutVector[i] = scalar*vector[i];
        }
        return outPutVector;
    }

    public static double[] vectorMultiplication(double[] vector1, double[] vector2){

        double outPutVector[] = new double[vector1.length];

        if(vector1.length != vector2.length){
            System.out.println("Vector dimension missmatch");
            return null;
        }
        for(int i = 0; i < vector1.length; i++){
            outPutVector[i] = vector1[i] * vector2[i];
        }
        return outPutVector;
    }

    public static double[] makeZeroVector(int length){
        double[] temp = new double[length];
        for (int i = 0; i < temp.length; i++){
            temp[i] = 0;
        }
        return temp;
    }

    public static double[][] makeZeroMatrix(int rows, int columns){
        double[][] temp = new double[rows][columns];
        for (int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[0].length; j++){
                temp[i][j] = 0;
            }
        }
        return temp;
    }

    public static double[][] AddMatrix(double[][] matrixOne, double[][] matrixTwo){

        double[][] addedMatrix = new double[matrixOne.length][matrixOne[0].length];
        for(int i = 0; i < matrixOne.length; i++){
            for(int j = 0; j < matrixOne[0].length; j++){
                addedMatrix[i][j] = matrixOne[i][j] + matrixTwo[i][j];
            }
        }
        return addedMatrix;
    }
    public static double GetPrediction(double[][] input){
        double prediction = 0;
        for (int i = 0; i < input.length; i++){
            for(int j = 0; j < input[0].length; j++){
                prediction += input[i][j];
            }
        }
        return prediction;
    }
    public static double[][] transposeMatrix(double[][] inputMatrix){
        double[][] transposedMatrix = new double[inputMatrix[0].length][inputMatrix.length];

        for(int i = 0; i < transposedMatrix.length; i ++){
            for(int j = 0; j < transposedMatrix[0].length; j ++){
                transposedMatrix[i][j] = inputMatrix[j][i];
            }
        }
        return transposedMatrix;
    }

    public static double GetPrediction(double[] input){
        double sum = 0;
        for(double value : input){
            sum += value;
        }
        return sum;
    }
    public static double[] dC_dW_Output(double dC_dA, double[] dA_dZ, double[][] dZ_dW){
        double[] dC_dW = new double[dZ_dW.length];

        for(int i = 0; i < dZ_dW.length; i++){
            for (int j = 0; j < dZ_dW[0].length; j++) {
                dC_dW[i] += dA_dZ[i] * dZ_dW[i][j] * dC_dA;
            }
        }
        return dC_dW;
    }

    public static double[] dC_dW_hidden(double[] dC_dW_prev, double[] dA_dZ, double[][] dZ_dW){
        double[] dC_dW = new double[dZ_dW.length];

        for(int i = 0; i < dZ_dW.length; i++){
            for (int j = 0; j < dZ_dW[0].length; j++) {
                dC_dW[i] = dA_dZ[i] * dZ_dW[i][j] * dC_dW_prev[i];
            }
        }
        return dC_dW;
    }
    public static double[] weightedSumAndBias(double[][] input, double[][] weights, double bias){
        double[] weightedSum = new double[input.length];

        for(int i = 0; i < input.length; i++){
            for(int j = 0; j < input[0].length; j++){
                weightedSum[i] += input[i][j] * weights[j][i];
            }
        }
        for(int i = 0; i < weightedSum.length; i++){
            weightedSum[i] += bias;
        }
       return weightedSum;
    }

    public static double[] ReluActivateNeuron(double[] weightedInput){
        double[] activatedOutput = new double[weightedInput.length];
        for(int i = 0; i < activatedOutput.length; i++) {
            if (weightedInput[i] <= 0) {
                activatedOutput[i] = 0;
                continue;
            }
            activatedOutput[i] = weightedInput[i];
        }
        return activatedOutput;
    }

    public static double[][] generateStartingWeights(int edgesIn, int edgesOut, int inputLength){

        Random random = new Random();

        double[][] generatedWeights = new double[edgesIn][inputLength];
        for (int i = 0; i < edgesIn; i++) {
            for (int j = 0; j < inputLength; j++) {
                double range = (Math.sqrt(6) / (Math.sqrt(edgesIn + edgesOut)));
                double minValue = -range;
                double maxValue = range;

                generatedWeights[i][j] = minValue + (maxValue - minValue) * random.nextDouble();
            }
        }
        return generatedWeights;
    }

    public static double[] outputSigmoidActivation(double[] vector){
        double[] activatedVector = new double[vector.length];

        for (int i = 0; i < activatedVector.length; i++) {
            activatedVector[i] = 1 / (1 + Math.exp(-vector[i]));
        }
        return activatedVector;

    }

    public static double dC_dA(double predictedValue, double expectedValue){
        double dC_dA = (predictedValue - expectedValue);
        return dC_dA;
    }

    public static double[] dA_dZ_sigmoid(double[] sigmoidVector){

        double[] sigmoidGradientVector = new double[sigmoidVector.length];

        for (int i = 0; i < sigmoidVector.length; i++) {
            sigmoidGradientVector[i] = sigmoidVector[i]*(1-sigmoidVector[i]);
        }
        return sigmoidGradientVector;
    }

    public static double[] dA_dZ_relu(double[] reluVector){

        double[] reluGradientVector = new double[reluVector.length];

        for (int i = 0; i < reluGradientVector.length; i++) {
            if(reluVector[i] <= 0){
                reluGradientVector[i] = 0;
                continue;
            }
            reluGradientVector[i] = 1;
        }
        return reluGradientVector;
    }

    public static double[][] getUpdatedWeights(double[][] weights, double[] dC_dW, double learnRate){
        double[][] updatedWeights = new double[weights.length][weights[0].length];

        for (int i = 0; i < updatedWeights.length; i++) {
            for (int j = 0; j < updatedWeights[0].length; j++) {
                updatedWeights[i][j] = weights[i][j] - learnRate*dC_dW[j];
             }
        }
        return updatedWeights;
    }

    public static double[] vectorAddition(double[] vector1, double[] vector2){
        double[] sum = new double[vector1.length];

        for (int i = 0; i < sum.length; i++) {
            sum[i] = vector1[i] + vector2[i];
        }
        return sum;
    }

    public static double vectorTrainingBigFiveCompability(double[][] input, double roundedBy){

        double[] vector1 = input[0];
        double[] vector2 = input[1];
        double[] matchVector = new double[vector1.length];
        double sum = 1;
        double ratio = 0;

        for (int i = 0; i < vector1.length; i++) {
            ratio = Math.sqrt(Math.pow(vector1[i]-vector2[i],2));
            //Openness
            if (i == 0){
                if(ratio <= 0.2){
                    matchVector[i] = 1;
                    continue;
                }
                if(ratio <= 0.4){
                    matchVector[i] = 0.75;
                    continue;
                }
                if(ratio <= 0.7){
                    matchVector[i] = 0.50;
                    continue;
                }
                if(ratio <= 1){
                    matchVector[i] = 0.25;
                    continue;
                }
            }
            //Conscientiousness
            if (i == 1){
                if(ratio <= 0.2){
                    matchVector[i] = 1;
                    continue;
                }
                if(ratio <= 0.4){
                    matchVector[i] = 0.75;
                    continue;
                }
                if(ratio <= 0.7){
                    matchVector[i] = 0.50;
                    continue;
                }
                if(ratio <= 1){
                    matchVector[i] = 0.25;
                    continue;
                }
            }
            //Extraversion
            if (i == 2){
                if(ratio <= 0.3){
                    matchVector[i] = 1;
                    continue;
                }
                if(ratio <= 0.6){
                    matchVector[i] = 0.75;
                    continue;
                }
                if(ratio <= 0.8){
                    matchVector[i] = 0.50;
                    continue;
                }
                if(ratio <= 1){
                    matchVector[i] = 0.25;
                    continue;
                }
            }
            //Agreeableness
            if (i == 3){
                if(ratio <= 0.2){
                    matchVector[i] = 1;
                    continue;
                }
                if(ratio <= 0.4){
                    matchVector[i] = 0.75;
                    continue;
                }
                if(ratio <= 0.7){
                    matchVector[i] = 0.50;
                    continue;
                }
                if(ratio <= 1){
                    matchVector[i] = 0.25;
                    continue;
                }
            }
            //Neuroticism
            if (i == 4){
                if(ratio <= 0.1){
                    matchVector[i] = 0.25;
                    continue;
                }
                if(ratio <= 0.2){
                    matchVector[i] = 0.50;
                    continue;
                }
                if(ratio <= 0.3){
                    matchVector[i] = 1;
                    continue;
                }
                if(ratio <= 0.4){
                    matchVector[i] = 0.75;
                    continue;
                }
                if(ratio <= 0.5){
                    matchVector[i] = 0.5;
                    continue;
                }
                if(ratio <= 0.6){
                    matchVector[i] = 0.5;
                    continue;
                }
                if(ratio <= 1){
                    matchVector[i] = 0.25;
                    continue;
                }
            }
        }

        for (int i = 0; i < matchVector.length; i++) {
            sum *= matchVector[i];
        }
        sum = Math.round(sum * roundedBy) / roundedBy;
        return sum;
    }

    public static double[][] createRandomTrainingInput(double roundedBy){

        double sum = 0;
        double[] bigFiveVector1 = new double[5];
        double[] bigFiveVector2 = new double[5];
        double v1 = 0;
        double v2 = 0;
        double[][] trainingMatrix = new double[2][bigFiveVector1.length];

        Random random = new Random();
        for (int i = 0; i < bigFiveVector1.length; i++) {
            v1 = random.nextDouble();
            v2 = random.nextDouble();
            bigFiveVector1[i] = Math.round(v1 * roundedBy) / roundedBy;
            bigFiveVector2[i] = Math.round(v2 * roundedBy) / roundedBy;
        }

        for (int i = 0; i < bigFiveVector1.length; i++) {
            trainingMatrix[0][i] = bigFiveVector1[i];
        }
        for (int i = 0; i < bigFiveVector2.length; i++) {
            trainingMatrix[1][i] = bigFiveVector2[i];
        }
        return trainingMatrix;
    }

}
