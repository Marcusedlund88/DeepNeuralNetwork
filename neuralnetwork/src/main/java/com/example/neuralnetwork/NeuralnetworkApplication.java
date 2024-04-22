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

    @Autowired
    private NeuralNetwork neuralNetwork;

    public static void main(String[] args) {
        SpringApplication.run(NeuralNetworkApplication.class, args);
    }

    @PostConstruct
    public void initializeNeuralNet(){

        double[][] initialData1 =
                {{1}, {1}, {1}, {1}};
        TrainingObject trainingObject1 = new TrainingObject(initialData1, 1);

        double[][] initialData2 =
                {{1}, {0.8}, {1}, {1}};
        TrainingObject trainingObject2 = new TrainingObject(initialData2, 0.8);

        double[][] initialData3 =
                {{1}, {1}, {0.5}, {1}};
        TrainingObject trainingObject3 = new TrainingObject(initialData3, 0.5);

        double[][] initialData4 =
                {{1}, {1}, {1}, {0}};
        TrainingObject trainingObject4 = new TrainingObject(initialData4, 0.0);

        double[][] initialData5 =
                {{0.7}, {0.5}, {0.2}, {1}};
        TrainingObject trainingObject5 = new TrainingObject(initialData5, 0.2);

        List<TrainingObject> trainingObjects = new ArrayList<>();
        trainingObjects.add(trainingObject1);
        trainingObjects.add(trainingObject2);
        trainingObjects.add(trainingObject3);
        trainingObjects.add(trainingObject4);
        //trainingObjects.add(trainingObject5);

        double expected = 1;

        MathOperations mathOperations = new StaticMathClass();

        double[][] expectedV = {{0.8}};

        neuralNetwork.createEmptyNetwork();
        for (int i = 0; i < 2000; i++) {
            neuralNetwork.propagateForward();
            neuralNetwork.propagateBackwards();
        }
        System.out.println("");

        for (int i = 0; i < 20000; i++) {
            Collections.shuffle(trainingObjects);
            for (TrainingObject to : trainingObjects) {
                double[][] trO = new double[1][1];
                trO[0][0] = to.expectedValue;

                neuralNetwork.setExpectedValue(trO);
                neuralNetwork.setInput(to.trainingInput);
                neuralNetwork.propagateForward();
                neuralNetwork.propagateBackwards();
            }
        }
    }
}
