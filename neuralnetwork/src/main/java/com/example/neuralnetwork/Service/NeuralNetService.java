package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Training.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NeuralNetService {

    private final NeuralNetwork neuralNetwork;

    @Autowired
    public NeuralNetService(NeuralNetwork neuralNetwork){
        this.neuralNetwork = neuralNetwork;
    }

    public void verifyMatch(InputObject inputObject){
        if(isValidRequest(inputObject)){
            try {
                neuralNetwork.propagateForward();
            }
            catch (PropagationException e){
                System.out.println("Error during forward propagation");
            }
        }
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
        return  inputObject.getInput() != null &&
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
}
