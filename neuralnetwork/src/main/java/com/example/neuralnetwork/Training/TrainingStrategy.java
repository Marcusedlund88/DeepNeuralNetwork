package com.example.neuralnetwork.Training;

import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;

import java.util.*;

public class TrainingStrategy {

    protected final NeuralNetwork neuralNetwork;
    protected List<TrainingObject> trainingObjects;
    protected final TrainingParam trainingParam;
    protected boolean isInstantiated = false;

    protected final int outputNodes = 1;
    protected final int numberOfTrainingObjects = 32;
    protected final int numberOfEpochs = 5000;

    public TrainingStrategy(NeuralNetwork neuralNetwork, TrainingParam trainingParam) {
        this.trainingParam = trainingParam;
        this.neuralNetwork = neuralNetwork;
        trainingObjects = new ArrayList<>();
    }

    public void startTraining() {
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
                if (trainingParam.getInputCase() == neuralNetwork.getCachedInputCase() && !trainingParam.getIsNewBatch()) {
                    trainingObjects = neuralNetwork.getTrainingObjects();
                } else {
                    neuralNetwork.setCachedInputCase(trainingParam.getInputCase());
                    setTrainingList(trainingParam.getNumberOfTrainingObjects());
                    neuralNetwork.setTrainingObjects(trainingObjects);
                }
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
                    }
                    neuralNetwork.setCacheMse(mse);
                } catch (PropagationException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }
        }
    }

    protected void setTrainingList(int numberOfTrainingObjects) {

    }

    protected void populateTrainingObjects(int count, double initialValue, double multiplier, Random random) {

    }

    public void initiateNeuralNetwork(TrainingParam trainingParam){

    }
}
