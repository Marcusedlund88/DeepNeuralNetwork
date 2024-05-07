package com.example.neuralnetwork;

import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.Math.StaticMathClass;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Training.TrainingObject;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class NeuralNetworkApplication {

    private NeuralNetwork neuralNetwork;

    public NeuralNetworkApplication(NeuralNetwork neuralNetwork){
        this.neuralNetwork = neuralNetwork;
    }

    public static void main(String[] args) {
        SpringApplication.run(NeuralNetworkApplication.class, args);
    }

    @PostConstruct
    public void initializeNeuralNet(){


    }
}
