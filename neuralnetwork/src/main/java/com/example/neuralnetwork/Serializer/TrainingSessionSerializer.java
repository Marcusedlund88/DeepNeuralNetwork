package com.example.neuralnetwork.Serializer;
import com.example.neuralnetwork.NeuralNetwork.Layer;
import com.google.gson.*;
import com.example.neuralnetwork.Data.TrainingSession;

import java.lang.reflect.Type;

public class TrainingSessionSerializer implements JsonSerializer<TrainingSession> {

    @Override
    public JsonObject serialize(TrainingSession trainingSession, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", trainingSession.getDate());

        JsonArray layersArray = new JsonArray();
        for (Layer layer : trainingSession.getLayers()) {
            layersArray.add(jsonSerializationContext.serialize(layer));
        }
        jsonObject.add("layers", layersArray);

        return jsonObject;
    }
}