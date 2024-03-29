package com.example.neuralnetwork;

import com.example.neuralnetwork.Math.StaticMathClass;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Training.TrainingObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class NeuralNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeuralNetworkApplication.class, args);

        double[][] initialData1 =
                {{1},{1},{1},{1}};
        TrainingObject trainingObject1 = new TrainingObject(initialData1, 1);

        double[][] initialData2 =
                {{0.8},{1},{1},{1}};
        TrainingObject trainingObject2 = new TrainingObject(initialData2, 0.8);

        double[][] initialData3 =
                {{1},{0.5},{1},{1}};
        TrainingObject trainingObject3 = new TrainingObject(initialData3, 0.5);

        double[][] initialData4 =
                {{0},{0}, {0},{0}};
        TrainingObject trainingObject4 = new TrainingObject(initialData4, 0);


        List<TrainingObject> trainingObjects = new ArrayList<>();
        trainingObjects.add(trainingObject1);
        trainingObjects.add(trainingObject2);
        //trainingObjects.add(trainingObject3);
        //trainingObjects.add(trainingObject4);

        double expected = 1;

        NeuralNetwork n = new NeuralNetwork(initialData1, 6, 20, 1, 1, 0.001);
            n.createEmptyNetwork();

            for(int i = 0; i < 20000; i++){
                Collections.shuffle(trainingObjects);
                for(TrainingObject to : trainingObjects){
                    n.setExpectedValue(to.expectedValue);
                    n.setInput(to.trainingInput);
                    n.propagateForward();
                    n.propagateBackwards();
                }
            }


    }
}
