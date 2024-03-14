package com.example.neuralnetwork;

import com.example.neuralnetwork.Math.StaticMathClass;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Training.TrainingObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class NeuralnetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeuralnetworkApplication.class, args);

        double[][] initialData1 =
                {{1}};
        TrainingObject trainingObject1 = new TrainingObject(initialData1, 1);

        double[][] initialData2 =
                {{0}};
        TrainingObject trainingObject2 = new TrainingObject(initialData1, 0);

        double[][] initialData3 =
                {{0}, {0}};
        TrainingObject trainingObject3 = new TrainingObject(initialData1, 0);

        double[][] initialData4 =
                {{0}, {0}};
        TrainingObject trainingObject4 = new TrainingObject(initialData1, 0);


        List<TrainingObject> trainingObjects = new ArrayList<>();


        double expected = 1;

        NeuralNetwork n = new NeuralNetwork(initialData1, 4, 20, 1, expected, 0.1);
            n.createEmptyNetwork();
            //n.populateNetworkWithStartingWeights();

            for(int i = 0; i < 21; i++){
                n.propagateForward();
                n.propagateBackwards();
            }
        System.out.println();
    }
}
