package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Training.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NeuralNetService {

    private final NeuralNetwork neuralNetwork;
    private final MathOperations mathOperations;

    @Autowired
    public NeuralNetService(NeuralNetwork neuralNetwork, MathOperations mathOperations){
        this.neuralNetwork = neuralNetwork;
        this.mathOperations = mathOperations;
    }

    public String verifyMatch(InputObject inputObject){
        if(isValidRequest(inputObject)){
            double[][] inputProduct = setInputForm(inputObject.getUserOneInput(),inputObject.getUserTwoInput());
            if(inputProduct != null) {
                try {
                    neuralNetwork.setInput(inputProduct);
                    neuralNetwork.propagateForward();
                    double matchValue = neuralNetwork.getPredictedValuePercent();
                    return String.valueOf(matchValue);
                } catch (PropagationException e) {
                    System.out.println("Error during forward propagation");
                }
            }
        }
        return "Error occurred";
    }

    public void StartTraining(TrainingParam trainingParam){
        if(isValidTrainingRequest(trainingParam)) {
            Training training = new Training(neuralNetwork, trainingParam);
            training.StartTraining();
            return;
        }
        System.out.println("Not correct input format");
    }

    private boolean isValidRequest(InputObject inputObject){
        return  inputObject.getUserOneInput() != null &&
                inputObject.getUserTwoInput() != null &&
                inputObject.getUserOneId() != null &&
                inputObject.getUserTwoId() != null;
    }

    private boolean isValidTrainingRequest(TrainingParam trainingParam){
        return trainingParam != null &&
                trainingParam.getNumberOfTrainingObjects() != 0 &&
                trainingParam.getNumberOfEpochs() != 0 &&
                trainingParam.getLearnRate() != 0 &&
                trainingParam.getNumberOfLayers() != 0 &&
                trainingParam.getHiddenLayerWidth() != 0 &&
                trainingParam.getNumberOfOutputNodes() != 0 &&
                trainingParam.getInputDataLength() != 0 &&
                trainingParam.getInputRows() != 0 &&
                trainingParam.getInputColumns() != 0 &&
                trainingParam.getShouldBuildNetwork() != null;
    }

    private double[][] setInputForm(double[] vector1, double[] vector2){
        if(vector1.length == vector2.length){
            double[][] output = new double[vector1.length][1];

            for(int i = 0; i < vector1.length; i++ ){
                output[i][0] =  Math.round((vector1[i] * vector2[i]) * 10.0) / 10.0;
            }
            return output;
        }
        return null;
    }
}
