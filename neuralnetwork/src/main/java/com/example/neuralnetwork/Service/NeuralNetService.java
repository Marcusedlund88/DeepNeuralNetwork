package com.example.neuralnetwork.Service;

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
}
