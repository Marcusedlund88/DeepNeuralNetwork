package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Data.RollbackRequest;
import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Data.TrainingSession;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Serializer.TrainingSessionSerializer;
import com.example.neuralnetwork.Training.CaseFiveTrainingStrategy;
import com.example.neuralnetwork.Training.CaseTenTrainingStrategy;
import com.example.neuralnetwork.Training.TrainingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoBulkWriteException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class NeuralNetService {

    private final NeuralNetwork neuralNetwork;
    private final MathOperations mathOperations;
    private final MongoTemplate mongoTemplate;
    private Gson gson;
    private TrainingParam.InputCase cacheInputCase;


    @Autowired
    public NeuralNetService(NeuralNetwork neuralNetwork, MathOperations mathOperations, MongoTemplate mongoTemplate){
        this.neuralNetwork = neuralNetwork;
        this.mathOperations = mathOperations;
        this.mongoTemplate = mongoTemplate;
    }

    public String verifyMatch(InputObject inputObject){
        if(isValidRequest(inputObject)){
            double[][] inputProduct = setInputForm(inputObject.getUserOneInput(),inputObject.getUserTwoInput());
            if(inputProduct != null) {
                try {
                    neuralNetwork.setInput(inputProduct);
                    neuralNetwork.propagateForward();
                    double matchValue = neuralNetwork.getPredictedValuePercent();
                    return String.valueOf(matchValue);
                } catch (PropagationException e) {
                    System.out.println("Error during forward propagation");
                }
            }
        }
        return "Error occurred";
    }

    public void StartTraining(TrainingParam trainingParam){
        if(cacheInputCase == null || cacheInputCase != trainingParam.getInputCase()){
            neuralNetwork.setCacheMse(999);
        }

        switch(trainingParam.getInputCase()) {
            case CASE_FIVE -> {
                TrainingStrategy caseFiveTrainingStrategy = new CaseFiveTrainingStrategy(neuralNetwork, trainingParam);
                caseFiveTrainingStrategy.initiateNeuralNetwork(trainingParam);
                caseFiveTrainingStrategy.startTraining();
                importTo("CaseFive", saveNeuralNetwork(neuralNetwork));
            }
            case CASE_TEN -> {
                TrainingStrategy caseTenTrainingStrategy = new CaseTenTrainingStrategy(neuralNetwork, trainingParam);
                caseTenTrainingStrategy.initiateNeuralNetwork(trainingParam);
                caseTenTrainingStrategy.startTraining();
                importTo("CaseTen", saveNeuralNetwork(neuralNetwork));
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
        Document document = mongoTemplate.findById(rollbackRequest.getId(),Document.class, rollbackRequest.getCollection());
        try{
            TrainingSession trainingSession = deserializeTrainingSession(document);
            neuralNetwork.rollbackNetwork(trainingSession.getLayers());
        }
        catch (Exception e){
            return "Error during deserialization";
        }
        return "Network load successful";
    }

    private List<Document> generateMongoDocs(List<String> lines) {
        List<Document> docs = new ArrayList<>();
        for (String json : lines) {
            docs.add(Document.parse(json));
        }
        return docs;
    }

    private int insertInto(String collection, List<Document> mongoDocs) {
        try {
            Collection<Document> inserts = mongoTemplate.insert(mongoDocs, collection);
            return inserts.size();
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof MongoBulkWriteException) {
                return ((MongoBulkWriteException) e.getCause())
                        .getWriteResult()
                        .getInsertedCount();
            }
            return 0;
        }
    }

    private String importTo(String collection, List<String> jsonLines) {
        List<Document> mongoDocs = generateMongoDocs(jsonLines);
        int inserts = insertInto(collection, mongoDocs);
        return inserts + "/" + jsonLines.size();
    }

    private List<String> saveNeuralNetwork(NeuralNetwork neuralNetwork){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(TrainingSession.class, new TrainingSessionSerializer());
        this.gson = gsonBuilder.create();

        List<String> jsonStrings = new ArrayList<>();
        jsonStrings.add(serializeTrainingSession(new TrainingSession(new Date().toString(), neuralNetwork.getLayers())));
        return jsonStrings;
    }

    private String serializeTrainingSession(TrainingSession trainingSession) {
        return gson.toJson(trainingSession);
    }

    private TrainingSession deserializeTrainingSession(Document document){
        gson = new Gson();
        String json = document.toJson();
        TrainingSession trainingSession = gson.fromJson(json, TrainingSession.class);
        return trainingSession;
    }
}
