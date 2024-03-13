package com.example.neuralnetwork;

import com.example.neuralnetwork.Math.StaticMathClass;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NeuralnetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeuralnetworkApplication.class, args);

    double[][] initialData =
            {{1}, {1}, {1}, {1}, {1}, {1}, {1}, {1}, {1}, {1}};
    double expected = 1;
    NeuralNetwork n = new NeuralNetwork(initialData, 4, 20, 1, expected, 0.9);
        n.createEmptyNetwork();
        n.populateNetworkWithStartingWeights();
        for (int i = 0; i < 20; i++) {
            n.propagateForward();
            n.propagateBackwards();
        }
    }
}
