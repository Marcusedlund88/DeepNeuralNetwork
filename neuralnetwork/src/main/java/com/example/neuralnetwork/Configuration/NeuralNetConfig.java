package com.example.neuralnetwork.Configuration;

import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.Math.StaticMathClass;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Training.TrainingObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NeuralNetConfig {

    @Bean
    public MathOperations mathOperations() {
        return new StaticMathClass();
    }

    @Bean
    public NeuralNetwork neuralNetwork(MathOperations mathOperations){

        double[][] initialData1 =
                {{1},{1},{1},{1}};
        TrainingObject trainingObject1 = new TrainingObject(initialData1, 1);
        double[][] expectedV = {{0.8}};

        return new NeuralNetwork(initialData1, 5, 8, 1,expectedV , 0.05, mathOperations);
    }

}
