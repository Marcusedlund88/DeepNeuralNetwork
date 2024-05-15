package com.example.neuralnetwork.Training;

import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.CreateEmptyNetworkException;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;

import java.util.*;

public class CaseFiveTrainingStrategy extends TrainingStrategy {

    private int rows = 5;
    private int columns = 1;
    private boolean isInstantiated = false;

    public CaseFiveTrainingStrategy(NeuralNetwork neuralNetwork, TrainingParam trainingParam) {
        super(neuralNetwork, trainingParam);
    }

    public void startTraining() {
        super.startTraining();
    }

    @Override
    protected void setTrainingList(int numberOfTrainingObjects) {

        Random random = new Random();
                populateTrainingObjects(numberOfTrainingObjects / 2, 0, 0.5, random);
                populateTrainingObjects(numberOfTrainingObjects / 2, 0.5, 0.5, random);
    }

    @Override
    protected void populateTrainingObjects(int count, double initialValue, double multiplier, Random random) {
        for (int i = 0; i < count; i++) {
            double[][] randomMatrix = new double[rows][columns];
            double minValue;
            for (int j = 0; j < rows; j++) {
                double[] randomArray = new double[columns];
                randomArray[0] = Math.round((initialValue + random.nextDouble() * multiplier) * 10) / 10.0;
                randomMatrix[j] = randomArray;
            }

            minValue = Arrays.stream(randomMatrix)
                    .flatMapToDouble(Arrays::stream)
                    .min()
                    .orElse(Double.NaN);
            trainingObjects.add(new TrainingObject(randomMatrix, minValue));
        }
    }

    @Override
    public void initiateNeuralNetwork(TrainingParam trainingParam) {

        trainingParam.setRows(rows);
        trainingParam.setColumns(columns);
        trainingParam.setNumberOfOutputNodes(outputNodes);
        trainingParam.setNumberOfEpochs(numberOfEpochs);
        trainingParam.setNumberOfTrainingObjects(numberOfTrainingObjects);

        try {
            neuralNetwork.setNeuralNetwork(trainingParam);
            super.isInstantiated = true;
        } catch (CreateEmptyNetworkException e) {
            System.out.println(e.getMessage());
            System.out.println("Error occurred");
        }
    }
}


