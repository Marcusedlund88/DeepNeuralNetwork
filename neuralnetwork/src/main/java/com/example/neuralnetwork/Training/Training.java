package com.example.neuralnetwork.Training;

import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.CreateEmptyNetworkException;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;

import java.util.*;

public class Training {

    private final NeuralNetwork neuralNetwork;
    private List<TrainingObject> trainingObjects;
    private final TrainingParam trainingParam;
    private int rows;
    private int columns;
    private boolean isInstantiated = false;

    public Training(NeuralNetwork neuralNetwork, TrainingParam trainingParam) {
        this.trainingParam = trainingParam;
        this.neuralNetwork = neuralNetwork;
    }

    public void StartTraining() {
        if (isInstantiated) {
            double mse;
            double cacheMse;

            //TODO: Fix verify if network is not yet created.
            if (!neuralNetwork.getIsNetworkUp()) {
                System.out.println("No network running");
                return;
            }
            cacheMse = neuralNetwork.getCacheMse();
            if (cacheMse <= 0.000000000001) {
                neuralNetwork.setCacheMse(999);
            }

            if (neuralNetwork.getTrainingObjects() == null) {
                setTrainingList(trainingParam.getNumberOfTrainingObjects());
                neuralNetwork.setTrainingObjects(trainingObjects);
            } else {
                trainingObjects = neuralNetwork.getTrainingObjects();
            }
            for (int i = 0; i < trainingParam.getNumberOfEpochs(); i++) {
                try {
                    mse = 0;
                    Collections.shuffle(trainingObjects);
                    for (TrainingObject to : trainingObjects) {

                        double[][] trO = new double[1][1];
                        trO[0][0] = to.expectedValue;
                        neuralNetwork.setExpectedValue(trO);
                        neuralNetwork.setInput(to.trainingInput);
                        neuralNetwork.setLearnRate(trainingParam.getLearnRate());
                        neuralNetwork.propagateForward();
                        neuralNetwork.propagateBackwards();
                        mse += neuralNetwork.getMse();
                    }
                    mse = mse / trainingObjects.size();

                    if (mse > neuralNetwork.getCacheMse()) {
                        neuralNetwork.saveNeuralNetwork(neuralNetwork);
                        System.out.println("Abort training");
                        break;
                    }
                    neuralNetwork.setCacheMse(mse);
                } catch (PropagationException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Error occurred");
                    break;
                }
            }
            //neuralNetwork.saveNeuralNetwork(neuralNetwork);
        }
    }

    private void setTrainingList(int numberOfTrainingObjects) {
        trainingObjects = new ArrayList<>();
        Random random = new Random();
        switch (trainingParam.getInputCase()) {
            case CASE_FIVE -> {
                populateTrainingObjects(numberOfTrainingObjects / 2, 0, 0.5, random);
                populateTrainingObjects(numberOfTrainingObjects / 2, 0.5,0.5, random);
            }
            case CASE_TEN -> {
                for (int i = 0; i < numberOfTrainingObjects; i++){
                    vectorTrainingBigFiveCompability(createRandomTrainingInput(10), 10);
                }
            }
        }
    }

    private void populateTrainingObjects(int count, double initialValue,  double multiplier, Random random) {
        for (int i = 0; i < count; i++) {
            double[][] randomMatrix = new double[rows][columns];
            double minValue;
            for (int j = 0; j < rows; j++) {
                double[] randomArray = new double[columns];
                randomArray[0] = Math.round((initialValue + random.nextDouble() * multiplier) * 10) / 10.0;
                randomMatrix[j] = randomArray;
            }
            switch (trainingParam.getInputCase()) {
                case CASE_FIVE -> {
                    minValue = Arrays.stream(randomMatrix)
                            .flatMapToDouble(Arrays::stream)
                            .min()
                            .orElse(Double.NaN);
                    trainingObjects.add(new TrainingObject(randomMatrix, minValue));
                }
                case CASE_TEN -> {

                }
            }
        }
    }

    public void initiateNeuralNetwork(TrainingParam trainingParam){
        rows = 0;
        columns = 0;

        switch(trainingParam.getInputCase()){
            case CASE_TEN -> {
                rows = 10;
                columns = 1;
                trainingParam.setRows(rows);
                trainingParam.setColumns(columns);
            }
            case CASE_FIVE -> {
                rows = 5;
                columns = 1;
                trainingParam.setRows(rows);
                trainingParam.setColumns(columns);
            }
        }

        try{
            neuralNetwork.setNeuralNetwork(trainingParam);
            isInstantiated = true;
        }
        catch(CreateEmptyNetworkException e){
            System.out.println(e.getMessage());
            System.out.println("Error occurred");
        }
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
