package com.example.neuralnetwork.Data;

import lombok.Data;

@Data
public class InputObject {
    private double[][] input;
    private String userOneId;
    private String userTwoId;

    public InputObject(double[][] input) {
        this.input = input;
    }

    public double[][] getInput() {
        return input;
    }

    public String getUserOneId() {
        return userOneId;
    }

    public String getUserTwoId() {
        return userTwoId;
    }
}
