package com.example.neuralnetwork.Data;

import lombok.Data;

@Data
public class TrainingRequest {

    private InputObject inputObject;

    public InputObject getInputObject() {
        return inputObject;
    }

    public void setInputObject(InputObject inputObject) {
        this.inputObject = inputObject;
    }
}
