package com.example.neuralnetwork.Data;

import lombok.Data;

@Data
public class InputObject {
    private double[] userOneInput;
    private double[] userTwoInput;
    private String userOneId;
    private String userTwoId;

    public InputObject(String userOneId, String userTwoId, double[] userOneInput, double[] userTwoInput) {
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
        this.userOneInput = userOneInput;
        this.userTwoInput = userTwoInput;
    }

    public double[] getUserOneInput() {
        return userOneInput;
    }

    public double[] getUserTwoInput() {
        return userTwoInput;
    }

    public String getUserOneId() {
        return userOneId;
    }

    public String getUserTwoId() {
        return userTwoId;
    }
}
