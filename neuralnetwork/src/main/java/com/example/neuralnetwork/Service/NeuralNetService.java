package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Data.TrainingSession;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Serializer.TrainingSessionSerializer;
import com.example.neuralnetwork.Training.Training;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoBulkWriteException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
            Training training = new Training(neuralNetwork, trainingParam);
            training.initiateNeuralNetwork(trainingParam);
            importTo("mycollection", saveNeuralNetwork(neuralNetwork));
            training.StartTraining();
    }

    private boolean isValidRequest(InputObject inputObject){
        return  inputObject.getUserOneInput() != null &&
                inputObject.getUserTwoInput() != null &&
                inputObject.getUserOneId() != null &&
                inputObject.getUserTwoId() != null;
    }

    private double[][] setInputForm(double[] vector1, double[] vector2){
        if(vector1.length == vector2.length){
            double[][] output = new double[vector1.length][1];

            for(int i = 0; i < vector1.length; i++ ){
                output[i][0] =  Math.round((vector1[i] * vector2[i]) * 10.0) / 10.0;
            }
            return output;
        }
        return null;
    }

    public String loadNetwork(){
        try {
            neuralNetwork.rollBackPreviousNetwork();
        }
        catch (Exception e){
            return "Oooops";
        }
        return "Success";
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

    public String importTo(String collection, List<String> jsonLines) {
        List<Document> mongoDocs = generateMongoDocs(jsonLines);
        int inserts = insertInto(collection, mongoDocs);
        return inserts + "/" + jsonLines.size();
    }

    public List<String> saveNeuralNetwork(NeuralNetwork neuralNetwork){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(TrainingSession.class, new TrainingSessionSerializer());
        this.gson = gsonBuilder.create();

        List<String> jsonStrings = new ArrayList<>();
        jsonStrings.add(serializeTrainingSession(new TrainingSession(new Date(), neuralNetwork.getLayers())));
        return jsonStrings;
    }

    public String serializeTrainingSession(TrainingSession trainingSession) {
        return gson.toJson(trainingSession);
    }
}
