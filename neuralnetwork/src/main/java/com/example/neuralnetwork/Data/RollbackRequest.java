package com.example.neuralnetwork.Data;

import lombok.Data;

@Data
public class RollbackRequest {

    private String collection;
    private String id;

    public String getCollection() {
        return collection;
    }

    public String getId() {
        return id;
    }
}
