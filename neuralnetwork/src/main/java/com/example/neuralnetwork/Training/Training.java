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

    public Training(NeuralNetwork neuralNetwork, TrainingParam trainingParam){
        this.trainingParam = trainingParam;
        this.neuralNetwork = neuralNetwork;
    }

    public void StartTraining() {
        if(isInstantiated) {
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

    private void setTrainingList(int numberOfTrainingObjects){
        trainingObjects = new ArrayList<>();
        Random random = new Random();
        populateTrainingObjects(numberOfTrainingObjects / 2, 0, random);
        populateTrainingObjects(numberOfTrainingObjects / 2, 0.5, random);
    }
    private void populateTrainingObjects(int count, double initialValue, Random random) {
        for (int i = 0; i < count; i++) {
            double[][] randomMatrix = new double[rows][columns];
            double minValue;
            for (int j = 0; j < rows; j++){
                double[] randomArray = new double[columns];
                randomArray[0] = Math.round((initialValue + random.nextDouble() * 0.5) * 10) / 10.0;
                randomMatrix[j] = randomArray;
            }
            minValue =  Arrays.stream(randomMatrix)
                    .flatMapToDouble(Arrays::stream)
                    .min()
                    .orElse(Double.NaN);
            trainingObjects.add(new TrainingObject(randomMatrix, minValue));
        }
    }

    public void initiateNeuralNetwork(TrainingParam trainingParam){
        rows = 0;
        columns = 0;

        switch(trainingParam.getInputCase()){
            case CASE_TEN -> {
                trainingParam.setInputDataLength(10);
                rows = 10;
                columns = 1;
            }
            case CASE_FIVE -> {
                trainingParam.setInputDataLength(5);
                rows = 5;
                columns = 1;
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
}
