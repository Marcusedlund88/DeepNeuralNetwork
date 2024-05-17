package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.TrainingSession;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import com.example.neuralnetwork.Serializer.TrainingSessionSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SerializationService {

    private Gson gson;

    public SerializationService(){
        gson = new Gson();
    }

    private String serializeTrainingSession(TrainingSession trainingSession) {
        return gson.toJson(trainingSession);
    }

    public TrainingSession deserializeTrainingSession(Document document){
        String json = document.toJson();
        TrainingSession trainingSession = gson.fromJson(json, TrainingSession.class);
        return trainingSession;
    }

    public List<String> saveNeuralNetwork(NeuralNetwork neuralNetwork){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(TrainingSession.class, new TrainingSessionSerializer());
        this.gson = gsonBuilder.create();

        List<String> jsonStrings = new ArrayList<>();
        jsonStrings.add(serializeTrainingSession(new TrainingSession(new Date().toString(), neuralNetwork.getLayers())));
        return jsonStrings;
    }
}
