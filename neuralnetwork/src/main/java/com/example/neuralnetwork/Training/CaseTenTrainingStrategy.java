package com.example.neuralnetwork.Training;

import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.CreateEmptyNetworkException;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;

import java.util.*;

public class CaseTenTrainingStrategy extends TrainingStrategy {

    private int rows = 10;
    private int columns = 1;
    private boolean isInstantiated = false;

    public CaseTenTrainingStrategy(NeuralNetwork neuralNetwork, TrainingParam trainingParam) {
        super(neuralNetwork, trainingParam);
    }

    public void startTraining() {
        super.startTraining();
    }

    @Override
    protected void setTrainingList(int numberOfTrainingObjects) {

        Random random = new Random();
        for (int i = 0; i < numberOfTrainingObjects; i++) {
                    vectorTrainingBigFiveCompability(createRandomTrainingInput(10), 10);
        }
    }

    @Override
    protected void populateTrainingObjects(int count, double initialValue,  double multiplier, Random random) {
    }

    public void initiateNeuralNetwork(TrainingParam trainingParam){

        trainingParam.setRows(rows);
        trainingParam.setColumns(columns);
        trainingParam.setNumberOfOutputNodes(outputNodes);
        trainingParam.setNumberOfEpochs(numberOfEpochs);
        trainingParam.setNumberOfTrainingObjects(numberOfTrainingObjects);

        try{
            neuralNetwork.setNeuralNetwork(trainingParam);
            super.isInstantiated = true;
        }
        catch(CreateEmptyNetworkException e){
            System.out.println(e.getMessage());
            System.out.println("Error occurred");
        }
    }

    /**
     * Calculates the compatibility score between two vectors based on the Big Five personality traits:
     * Openness, Conscientiousness, Extroversion, Agreeableness, and Neuroticism.
     * Compatibility scores are assigned based on the similarity of corresponding traits.
     *
     * @param input     A 2D array representing two vectors of Big Five personality trait values.
     * @param roundedBy The number of decimal places to round the compatibility score.
     */
    private void vectorTrainingBigFiveCompability(double[][] input, double roundedBy){

        double[] vector1 = input[0];
        double[] vector2 = input[1];
        double[] matchVector = new double[vector1.length];
        double sum = 1;
        double ratio;

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
            //Extroversion
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
                }
            }
        }

        for (double value : matchVector) {
            sum *= value;
        }
        sum = Math.round(sum * roundedBy) / roundedBy;


        int[] result = new int[vector1.length + vector2.length];

        double[][] randomMatrix = new double[rows][columns];

        for (int i = 0; i < vector1.length; i++){
            randomMatrix[i][0] = vector1[i];
        }
        for (int i = 0; i < vector2.length; i++) {
            randomMatrix[i + vector2.length -1][0] = vector2[i];
        }
        trainingObjects.add(new TrainingObject(randomMatrix, sum));
    }

    /**
     * Generates a random training input matrix for a neural network.
     * Each row of the matrix represents a vector of Big Five personality traits.
     * The traits are randomly generated within the range [0, 1] and rounded to the specified precision.
     *
     * @param roundedBy The precision to which the generated values should be rounded.
     * @return A 2D array representing the random training input matrix.
     */
    private double[][] createRandomTrainingInput(double roundedBy){

        double sum;
        double[] bigFiveVector1 = new double[5];
        double[] bigFiveVector2 = new double[5];
        double v1;
        double v2;
        double[][] trainingMatrix = new double[2][bigFiveVector1.length];

        Random random = new Random();
        for (int i = 0; i < bigFiveVector1.length; i++) {
            v1 = random.nextDouble();
            v2 = random.nextDouble();
            bigFiveVector1[i] = Math.round(v1 * roundedBy) / roundedBy;
            bigFiveVector2[i] = Math.round(v2 * roundedBy) / roundedBy;
        }

        System.arraycopy(bigFiveVector1, 0, trainingMatrix[0], 0, bigFiveVector1.length);
        System.arraycopy(bigFiveVector2, 0, trainingMatrix[1], 0, bigFiveVector2.length);

        return trainingMatrix;
    }
}
