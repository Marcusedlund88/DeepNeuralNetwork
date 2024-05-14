package com.example.neuralnetwork.Serializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.example.neuralnetwork.Data.TrainingSession;

import java.lang.reflect.Type;

public class TrainingSessionSerializer implements JsonSerializer<TrainingSession> {

    @Override
    public JsonObject serialize(TrainingSession trainingSession, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", trainingSession.getId());
        jsonObject.addProperty("date", trainingSession.getDate().toString());

        JsonObject layersObject = new JsonObject();
        for (int i = 0; i < trainingSession.getLayers().length; i++) {
            layersObject.add("layer" + (i + 1), jsonSerializationContext.serialize(trainingSession.getLayers()[i]));
        }
        jsonObject.add("layers", layersObject);

        return jsonObject;
    }
}