package com.example.neuralnetwork;

import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Service.NeuralNetService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NeuralNetworkApplication {

    private NeuralNetwork neuralNetwork;
    private NeuralNetService neuralNetService;

    public NeuralNetworkApplication(NeuralNetwork neuralNetwork, NeuralNetService neuralNetService){
        this.neuralNetwork = neuralNetwork;
        this.neuralNetService = neuralNetService;
    }

    public static void main(String[] args) {
        SpringApplication.run(NeuralNetworkApplication.class, args);
    }
}
