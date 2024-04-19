package com.example.neuralnetwork.Math;

import java.util.Random;

public class StaticMathClass implements MathOperations{

    public double[] fillVectorWithSameValue(int columns, int value){
        double[] temp = new double[columns];
        for (int i = 0; i < temp.length; i++){
            temp[i] = value;
        }
        return temp;
    }

    public double[][] fillMatrixWithSameValue(int rows, int columns, int value){
        double[][] temp = new double[rows][columns];
        for (int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[0].length; j++){
                temp[i][j] = value;
            }
        }
        return temp;
    }

    public double generateRandomBias(int edgesIn, int edgesOut) {
        Random random = new Random();

        double bias = 0;
        double range = (Math.sqrt(6) / (Math.sqrt(edgesIn + edgesOut)));
        double minValue = -range;
        double maxValue = range;

        bias =minValue + (maxValue - minValue) * random.nextDouble();


        return bias;
    }
    public double updateBias(double learnRate, double bias, double[] dC_dB){
        double meanBias = 0;

        for (int i = 0; i < dC_dB.length; i++) {
            meanBias += dC_dB[i];
        }

        return bias - learnRate*(meanBias/dC_dB.length);
    }
    public double[] vectorMatrixMultiplication(double[] vector, double[][] matrix){
        double[] temp = new double[matrix.length];

        for (int i = 0; i < temp.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                temp[i] += vector[j]*matrix[i][j];
            }
        }
        return temp;
    }

    public double[] vectorScalarMultiplication(double scalar, double[] vector){
        double outPutVector[] = new double[vector.length];
        for(int i = 0; i < outPutVector.length; i++){
            outPutVector[i] = scalar*vector[i];
        }
        return outPutVector;
    }

    public double[] elementWiseVectorMultiplication(double[] vector1, double[] vector2){

        double outPutVector[] = new double[vector2.length];

        for(int i = 0; i < vector1.length; i++){
            for (int j = 0; j < vector2.length; j++) {
                outPutVector[j] += vector1[i] * vector2[j];
            }

        }
        return outPutVector;
    }

    public double[] makeZeroVector(int length){
        double[] temp = new double[length];
        for (int i = 0; i < temp.length; i++){
            temp[i] = 0;
        }
        return temp;
    }

    public double[][] makeZeroMatrix(int rows, int columns){
        double[][] temp = new double[rows][columns];
        for (int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[0].length; j++){
                temp[i][j] = 0;
            }
        }
        return temp;
    }

    public double[][] AddMatrix(double[][] matrixOne, double[][] matrixTwo){

        double[][] addedMatrix = new double[matrixOne.length][matrixOne[0].length];
        for(int i = 0; i < matrixOne.length; i++){
            for(int j = 0; j < matrixOne[0].length; j++){
                addedMatrix[i][j] = matrixOne[i][j] + matrixTwo[i][j];
            }
        }
        return addedMatrix;
    }
    public double[][] GetPrediction(double[][] input){
        double[][] prediction = new double[input.length][input[0].length];
        for (int i = 0; i < input.length; i++){
            for(int j = 0; j < input[0].length; j++){
                prediction[i][j] += input[i][j];
            }
        }
        return prediction;
    }
    public double[][] transposeMatrix(double[][] inputMatrix){
        double[][] transposedMatrix = new double[inputMatrix[0].length][inputMatrix.length];

        for(int i = 0; i < transposedMatrix.length; i ++){
            for(int j = 0; j < transposedMatrix[0].length; j ++){
                transposedMatrix[i][j] = inputMatrix[j][i];
            }
        }
        return transposedMatrix;
    }

    public double GetPrediction(double[] input){
        double sum = 0;
        for(double value : input){
            sum += value;
        }
        return sum;
    }

    public double[] weightedSum(double[][] input, double[][] weights){
        double[] weightedSum = new double[input.length];

        for(int i = 0; i < input.length; i++){
            for(int j = 0; j < input[0].length; j++){
                weightedSum[i] += input[i][j] * weights[j][i];
            }
        }
       return weightedSum;
    }

    public double[] ReluActivateNeuron(double[] weightedInput){
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

    public double[] outputSigmoidActivation(double[] vector){
        double[] activatedVector = new double[vector.length];

        for (int i = 0; i < activatedVector.length; i++) {
            activatedVector[i] = 1 / (1 + Math.exp(-vector[i]));
        }
        return activatedVector;

    }

    public double[] outputSoftMaxActivation(double[] vector){
        double[] activatedVector = new double[vector.length];
        double sum = 0;
        for(double value : vector){
            sum += Math.exp(value);
        }

        for (int i = 0; i < vector.length; i++) {
            activatedVector[i] = Math.exp(vector[i])/sum;
        }
        return activatedVector;
    }

    public double[][] dC_dA(double[][] predictedValue, double[][] expectedValue){
        double dC_dA = 0;
        int n = predictedValue[0].length;
        double[] tempVector  = new double[predictedValue[0].length];
        double[][] tempMatrix = new double[predictedValue.length][predictedValue[0].length];
        double totalLoss = 0.0000;

// Iterate over each element in the predicted and expected values matrices
        for (int i = 0; i < predictedValue.length; i++) {
            for (int j = 0; j < predictedValue[0].length; j++) {
                tempVector[j] = 2.0/n*(predictedValue[i][j] - expectedValue[i][j]);
            }
            tempMatrix[i] = tempVector;
            tempVector  = new double[predictedValue[0].length];
            totalLoss = 0;
        }

// Calculate the average loss
        double averageLoss = totalLoss / (predictedValue.length);

        return tempMatrix;
    }

    public double[] dA_dZ_relu(double[] reluVector){

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

    public double[] vectorAddition(double[] vector1, double[] vector2){
        double[] sum = new double[vector1.length];

        for (int i = 0; i < sum.length; i++) {
                if(vector2 == null) vector2[i] = 0;
                sum[i] = vector1[i] + vector2[0];
        }
        return sum;
    }

    public double vectorTrainingBigFiveCompability(double[][] input, double roundedBy){

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

    public double[][] createRandomTrainingInput(double roundedBy){

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

    public double[][] normalizeInput(double[][] input) {
        int rows = input.length;
        int cols = input[0].length;

        double[] minValues = new double[cols];
        double[] maxValues = new double[cols];

        for (int j = 0; j < cols; j++) {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (int i = 0; i < rows; i++) {
                min = Math.min(min, input[i][j]);
                max = Math.max(max, input[i][j]);
            }
            minValues[j] = min;
            maxValues[j] = max;
        }

        double[][] scaledData = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double range = maxValues[j] - minValues[j];

                if (range == 0) {
                    scaledData[i][j] = 1;
                } else {
                    scaledData[i][j] = (input[i][j] - minValues[j]) / range;
                }
            }
        }

        return scaledData;
    }
}
