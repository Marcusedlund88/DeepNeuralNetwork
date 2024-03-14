package com.example.neuralnetwork.Training;

public class TrainingObject {

    public double[][] trainingInput;
    public double expectedValue;

    public TrainingObject(double[][] trainingInput, double expectedValue) {
        this.trainingInput = trainingInput;
        this.expectedValue = expectedValue;
    }
}
