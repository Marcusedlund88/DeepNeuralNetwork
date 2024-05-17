package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.RollbackRequest;
import com.example.neuralnetwork.Data.TrainingSession;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.mongodb.MongoBulkWriteException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MongoDBService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoDBService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
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

    public String loadNetwork(RollbackRequest rollbackRequest){
        Document document = mongoTemplate.findById(rollbackRequest.getId(),Document.class, rollbackRequest.getCollection());
        try{
            //TrainingSession trainingSession = deserializeTrainingSession(document);
            //neuralNetwork.rollbackNetwork(trainingSession.getLayers());
        }
        catch (Exception e){
            return "Error during deserialization";
        }
        return "Network load successful";
    }

    public Document fetchNeuralNetwork(RollbackRequest rollbackRequest){
        try {
            return mongoTemplate.findById(rollbackRequest.getId(), Document.class, rollbackRequest.getCollection());
        }
        catch (Exception e){
            System.out.println("Error fetching network");
        }
        return null;
    }
}
