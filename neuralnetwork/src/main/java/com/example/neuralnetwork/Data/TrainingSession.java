package com.example.neuralnetwork.Data;

import com.example.neuralnetwork.NeuralNetwork.Layer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collation = "mycollection")
public class TrainingSession {

    @Id
    private String Id;
    private Date date;
    private Layer[] layers;

    public TrainingSession(Date date, Layer[] layers){
        this.date = date;
        this.layers = layers;
    }

    public String getId() {
        return Id;
    }

    public Date getDate() {
        return date;
    }

    public Layer[] getLayers() {
        return layers;
    }
}
