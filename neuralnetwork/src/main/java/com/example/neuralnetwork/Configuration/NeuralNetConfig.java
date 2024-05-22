package com.example.neuralnetwork.Configuration;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.Math.StaticMathOperations;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NeuralNetConfig {

    @Bean
    public MathOperations mathOperations() {
        return new StaticMathOperations();
    }

    @Bean
    public InputObject inputObject(){
        return null;
    }

    @Bean
    public NeuralNetwork neuralNetwork(MathOperations mathOperations){
        return new NeuralNetwork(mathOperations);
    }
}
