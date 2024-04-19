package com.example.neuralnetwork.Math;

import java.util.Random;

public class StaticMathClass implements MathOperations{

    /**
     * Fills a vector with the same specified value.
     *
     * @param columns The number of elements in the vector.
     * @param value The value to fill the vector with.
     * @return An array representing the vector filled with the specified value.
     */
    public double[] fillVectorWithSameValue(int columns, int value){
        double[] temp = new double[columns];
        for (int i = 0; i < temp.length; i++){
            temp[i] = value;
        }
        return temp;
    }

    /**
     * Fills a matrix with the same specified value.
     *
     * @param rows The number of rows in the matrix.
     * @param columns The number of columns in the matrix.
     * @param value The value to fill the matrix with.
     * @return An array representing the matrix filled with the specified value.
     */
    public double[][] fillMatrixWithSameValue(int rows, int columns, int value){
        double[][] temp = new double[rows][columns];
        for (int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[0].length; j++){
                temp[i][j] = value;
            }
        }
        return temp;
    }

    /**
     * Generates a random bias value using Xavier/Glorot initialization.
     *
     * @param edgesIn The number of incoming edges to the neuron.
     * @param edgesOut The number of outgoing edges from the neuron.
     * @return A random bias value initialized according to Xavier/Glorot initialization.
     */
    public double generateRandomBias(int edgesIn, int edgesOut) {
        Random random = new Random();

        double bias = 0;
        double range = (Math.sqrt(6) / (Math.sqrt(edgesIn + edgesOut)));
        double minValue = -range;
        double maxValue = range;

        bias =minValue + (maxValue - minValue) * random.nextDouble();


        return bias;
    }

    /**
     * Updates the bias value using gradient descent.
     *
     * @param learnRate The learning rate (alpha) used for updating the bias.
     * @param bias The current bias value.
     * @param dC_dB The gradient of the cost function with respect to the bias.
     * @return The updated bias value after applying gradient descent.
     */
    public double updateBias(double learnRate, double bias, double[] dC_dB){
        double meanBias = 0;

        for (int i = 0; i < dC_dB.length; i++) {
            meanBias += dC_dB[i];
        }

        return bias - learnRate*(meanBias/dC_dB.length);
    }

    /**
     * Performs vector-matrix multiplication.
     *
     * @param vector The vector to be multiplied.
     * @param matrix The matrix to be multiplied.
     * @return The result of multiplying the vector by the matrix.
     * @throws IllegalArgumentException if the dimensions of the vector and matrix are incompatible for multiplication.
     */
    public double[] vectorMatrixMultiplication(double[] vector, double[][] matrix){
        double[] temp = new double[matrix.length];

        for (int i = 0; i < temp.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                temp[i] += vector[j]*matrix[i][j];
            }
        }
        return temp;
    }

    /**
     * Performs scalar-vector multiplication.
     *
     * @param scalar The scalar value to multiply the vector by.
     * @param vector The vector to be multiplied by the scalar.
     * @return The result of multiplying the vector by the scalar.
     */
    public double[] vectorScalarMultiplication(double scalar, double[] vector){
        double outPutVector[] = new double[vector.length];
        for(int i = 0; i < outPutVector.length; i++){
            outPutVector[i] = scalar*vector[i];
        }
        return outPutVector;
    }

    /**
     * Performs element-wise vector multiplication.
     *
     * @param vector1 The first vector for element-wise multiplication.
     * @param vector2 The second vector for element-wise multiplication.
     * @return The result of element-wise multiplication of the two vectors.
     * @throws IllegalArgumentException if the dimensions of the vectors are not equal.
     */
    public double[] elementWiseVectorMultiplication(double[] vector1, double[] vector2){

        double outPutVector[] = new double[vector2.length];

        for(int i = 0; i < vector1.length; i++){
            for (int j = 0; j < vector2.length; j++) {
                outPutVector[j] += vector1[i] * vector2[j];
            }

        }
        return outPutVector;
    }

    /**
     * Creates a zero-vector.
     *
     * @param length The length of the vector.
     * @return A zero-vector of length "length".
     */
    public double[] makeZeroVector(int length){
        double[] temp = new double[length];
        for (int i = 0; i < temp.length; i++){
            temp[i] = 0;
        }
        return temp;
    }

    /**
     * Creates a zero-matrix.
     *
     * @param rows The number of rows.
     * @param columns The number of columns
     * @return A zero-matrix with dimensions rows x columns.
     */
    public double[][] makeZeroMatrix(int rows, int columns){
        double[][] temp = new double[rows][columns];
        for (int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[0].length; j++){
                temp[i][j] = 0;
            }
        }
        return temp;
    }

    /**
     * Adds two matrices element-wise.
     *
     * @param matrixOne The first matrix to be added.
     * @param matrixTwo The second matrix to be added.
     * @return The result of element-wise addition of the two matrices.
     * @throws IllegalArgumentException if the dimensions of the matrices are not equal.
     */
    public double[][] AddMatrix(double[][] matrixOne, double[][] matrixTwo){

        double[][] addedMatrix = new double[matrixOne.length][matrixOne[0].length];
        for(int i = 0; i < matrixOne.length; i++){
            for(int j = 0; j < matrixOne[0].length; j++){
                addedMatrix[i][j] = matrixOne[i][j] + matrixTwo[i][j];
            }
        }
        return addedMatrix;
    }

    /**
     * Gets the prediction from the input matrix.
     *
     * @param input The input matrix.
     * @return The prediction matrix obtained from the input matrix.
     */
    public double[][] GetPrediction(double[][] input){
        double[][] prediction = new double[input.length][input[0].length];
        for (int i = 0; i < input.length; i++){
            for(int j = 0; j < input[0].length; j++){
                prediction[i][j] += input[i][j];
            }
        }
        return prediction;
    }

    /**
     * Transposes the input matrix.
     *
     * @param inputMatrix The input matrix to be transposed.
     * @return The transposed matrix.
     */
    public double[][] transposeMatrix(double[][] inputMatrix){
        double[][] transposedMatrix = new double[inputMatrix[0].length][inputMatrix.length];

        for(int i = 0; i < transposedMatrix.length; i ++){
            for(int j = 0; j < transposedMatrix[0].length; j ++){
                transposedMatrix[i][j] = inputMatrix[j][i];
            }
        }
        return transposedMatrix;
    }

    /**
     * Gets the prediction from the input vector.
     *
     * @param input The input vector.
     * @return The prediction obtained from the input vector.
     */
    public double GetPrediction(double[] input){
        double sum = 0;
        for(double value : input){
            sum += value;
        }
        return sum;
    }

    /**
     * Calculates the weighted sum of inputs using the provided weights.
     *
     * @param input The input matrix.
     * @param weights The weight matrix.
     * @return The weighted sum array.
     * @throws IllegalArgumentException if the dimensions of the input and weights matrices are not compatible.
     */

    public double[] weightedSum(double[][] input, double[][] weights){
        double[] weightedSum = new double[input.length];

        for(int i = 0; i < input.length; i++){
            for(int j = 0; j < input[0].length; j++){
                weightedSum[i] += input[i][j] * weights[j][i];
            }
        }
       return weightedSum;
    }

    /**
     * Applies the ReLU activation function to the weighted inputs.
     * ReLU (Rectified Linear Unit) sets all negative values to zero and keeps positive values unchanged.
     *
     * @param weightedInput The array of weighted inputs.
     * @return The array of activated outputs after applying the ReLU activation function.
     */
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

    /**
     * Applies the sigmoid activation function to the input vector.
     * Sigmoid activation function squashes the input values to the range [0, 1].
     *
     * @param vector The input vector.
     * @return The array of activated outputs after applying the sigmoid activation function.
     */
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

    /**
     * Computes the gradient of the loss function with respect to the predicted values.
     * This method calculates the gradient of the mean squared error loss function.
     *
     * @param predictedValue The predicted values produced by the neural network.
     * @param expectedValue  The expected values.
     * @return The gradient matrix of the loss function with respect to the predicted values.
     */
    public double[][] dC_dA(double[][] predictedValue, double[][] expectedValue){
        double dC_dA = 0;
        int n = predictedValue[0].length;
        double[] tempVector  = new double[predictedValue[0].length];
        double[][] tempMatrix = new double[predictedValue.length][predictedValue[0].length];
        double totalLoss = 0.0000;

        for (int i = 0; i < predictedValue.length; i++) {
            for (int j = 0; j < predictedValue[0].length; j++) {
                tempVector[j] = 2.0/n*(predictedValue[i][j] - expectedValue[i][j]);
            }
            tempMatrix[i] = tempVector;
            tempVector  = new double[predictedValue[0].length];
            totalLoss = 0;
        }

        double averageLoss = totalLoss / (predictedValue.length);

        return tempMatrix;
    }

    /**
     * Computes the gradient of the ReLU activation function with respect to its input.
     * The ReLU activation function is defined as f(x) = max(0, x).
     *
     * @param reluVector The input vector to the ReLU activation function.
     * @return The gradient vector of the ReLU activation function with respect to its input.
     */
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

    /**
     * Performs element-wise addition of two vectors.
     *
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return The sum of the two input vectors.
     */
    public double[] vectorAddition(double[] vector1, double[] vector2){
        double[] sum = new double[vector1.length];

        for (int i = 0; i < sum.length; i++) {
                if(vector2 == null) vector2[i] = 0;
                sum[i] = vector1[i] + vector2[i];
        }
        return sum;
    }

    /**
     * Calculates the compatibility score between two vectors based on the Big Five personality traits:
     * Openness, Conscientiousness, Extraversion, Agreeableness, and Neuroticism.
     * Compatibility scores are assigned based on the similarity of corresponding traits.
     *
     * @param input     A 2D array representing two vectors of Big Five personality trait values.
     * @param roundedBy The number of decimal places to round the compatibility score.
     * @return The compatibility score between the two vectors, rounded to the specified decimal places.
     */
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

    /**
     * Generates a random training input matrix for a neural network.
     * Each row of the matrix represents a vector of Big Five personality traits.
     * The traits are randomly generated within the range [0, 1] and rounded to the specified precision.
     *
     * @param roundedBy The precision to which the generated values should be rounded.
     * @return A 2D array representing the random training input matrix.
     */
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
}
