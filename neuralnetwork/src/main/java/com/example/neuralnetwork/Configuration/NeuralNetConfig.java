package com.example.neuralnetwork.Configuration;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.Math.StaticMathClass;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NeuralNetConfig {

    @Bean
    public MathOperations mathOperations() {
        return new StaticMathClass();
    }

    @Bean
    public InputObject inputObject(){
        double[][] expectedV = {{0.8}};
        double[][] initialData1 =
                {{1},{1},{1},{1}};
        return new InputObject(initialData1, 5, 8,1,expectedV,0.05);
    };

    @Bean
    public NeuralNetwork neuralNetwork(MathOperations mathOperations){

        return new NeuralNetwork(inputObject(), mathOperations);
    }

}
