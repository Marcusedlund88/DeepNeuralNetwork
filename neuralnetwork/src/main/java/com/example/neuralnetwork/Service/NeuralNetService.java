package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NeuralNetService {

    private final NeuralNetwork neuralNetwork;

    @Autowired
    public NeuralNetService(NeuralNetwork neuralNetwork){
        this.neuralNetwork = neuralNetwork;
    }

    public void processRequest(double[][] requestArray){
        neuralNetwork.setInput(requestArray);
        neuralNetwork.propagateForward();
    }

    public void processTrainingRequest(InputObject inputObject){
        neuralNetwork.setInput(inputObject.getInput());
        neuralNetwork.setExpectedValue(inputObject.getExpectedValue());
        neuralNetwork.propagateForward();
        neuralNetwork.propagateBackwards();
    }
}
