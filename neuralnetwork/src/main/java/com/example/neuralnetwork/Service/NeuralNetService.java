package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Data.RollbackRequest;
import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Data.TrainingSession;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.Math.BigFiveCalculator;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Training.CaseFiveTrainingStrategy;
import com.example.neuralnetwork.Training.CaseTenTrainingStrategy;
import com.example.neuralnetwork.Training.TrainingStrategy;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NeuralNetService {

    private final NeuralNetwork neuralNetwork;
    private final MathOperations mathOperations;
    private final MongoDBService mongoDBService;
    private final SerializationService serializationService;
    private Gson gson;
    private TrainingParam.InputCase cacheInputCase;


    @Autowired
    public NeuralNetService(NeuralNetwork neuralNetwork,
                            MathOperations mathOperations,
                            MongoDBService mongoDBService,
                            SerializationService serializationService){
        this.neuralNetwork = neuralNetwork;
        this.mathOperations = mathOperations;
        this.mongoDBService = mongoDBService;
        this.serializationService = serializationService;
    }

    @PostConstruct
    public void initializedLatestApprovedNetwork(){
        try {
            fetchLatestNetwork();
        }
        catch (Exception e){
            System.out.println("Error fetching latest network");
        }
    }

    public String verifyMatch(InputObject inputObject){
        if(isValidRequest(inputObject)){
            double[][] inputProduct = setInputForm(inputObject.getUserOneInput(),inputObject.getUserTwoInput());
            if(inputProduct != null) {
                try {
                    neuralNetwork.setInput(inputProduct);
                    neuralNetwork.propagateForward();
                    double matchValue = neuralNetwork.getPredictedValuePercent();
                    double actualValue = BigFiveCalculator.bigFiveCompability(inputProduct, 10);
                    return String.valueOf("Predicted value: " + matchValue + " actual value: "+ actualValue);
                } catch (PropagationException e) {
                    System.out.println("Error during forward propagation");
                }
            }
        }
        return "Error occurred";
    }

    public void startTraining(TrainingParam trainingParam){
        if(cacheInputCase == null || cacheInputCase != trainingParam.getInputCase()){
            neuralNetwork.setCacheMse(999);
        }

        switch(trainingParam.getInputCase()) {
            case CASE_FIVE -> {
                TrainingStrategy caseFiveTrainingStrategy = new CaseFiveTrainingStrategy(neuralNetwork, trainingParam);
                caseFiveTrainingStrategy.initiateNeuralNetwork(trainingParam);
                caseFiveTrainingStrategy.startTraining();
                mongoDBService.importTo("CaseFive", serializationService.saveNeuralNetwork(neuralNetwork));
            }
            case CASE_TEN -> {
                TrainingStrategy caseTenTrainingStrategy = new CaseTenTrainingStrategy(neuralNetwork, trainingParam);
                caseTenTrainingStrategy.initiateNeuralNetwork(trainingParam);
                caseTenTrainingStrategy.startTraining();
                mongoDBService.importTo("CaseTen", serializationService.saveNeuralNetwork(neuralNetwork));
            }
        }
    }

    private boolean isValidRequest(InputObject inputObject){
        return  inputObject.getUserOneInput() != null &&
                inputObject.getUserTwoInput() != null &&
                inputObject.getUserOneId() != null &&
                inputObject.getUserTwoId() != null;
    }

    private double[][] setInputForm(double[] vector1, double[] vector2){
        if(vector1.length == vector2.length) {
            if (neuralNetwork.getLayers()[0].getNumberOfNeurons() == 5) {
                double[][] output = new double[vector1.length][1];

                for (int i = 0; i < vector1.length; i++) {
                    output[i][0] = Math.round((vector1[i] * vector2[i]) * 10.0) / 10.0;
                }
                return output;
            }
            if(neuralNetwork.getLayers()[0].getNumberOfNeurons() == 10){
                double[][] output = new double[vector1.length + vector2.length][1];

                int counter  = 0;
                for(double value : vector1){
                    output[counter][0] = vector1[counter];
                    counter++;
                }
                for(double value : vector2){
                    output[counter][0] = vector1[counter-vector1.length];
                    counter++;
                }
                return output;
            }
        }
        return null;
    }

   public String loadNetwork(RollbackRequest rollbackRequest){
        Document document = mongoDBService.fetchNeuralNetworkByID(rollbackRequest);
        try{
            TrainingSession trainingSession = serializationService.deserializeTrainingSession(document);
            neuralNetwork.rollbackNetwork(trainingSession.getLayers());
        }
        catch (Exception e){
            return "Error during deserialization";
        }
        return "Network load successful";
    }

    private String fetchLatestNetwork(){
        Document document = mongoDBService.fetchLatestNeuralNetwork();
        try{
            TrainingSession trainingSession = serializationService.deserializeTrainingSession(document);
            neuralNetwork.rollbackNetwork(trainingSession.getLayers());
        }
        catch (Exception e){
            return "Error during deserialization";
        }
        return "Network load successful";
    }
}
