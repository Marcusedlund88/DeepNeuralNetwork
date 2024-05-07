package com.example.neuralnetwork.Data;

import lombok.Data;

@Data
public class TrainingParam {

    private int numberOfTrainingObjects;
    private int numberOfEpochs;
    private double learnRate;
    private int numberOfNeuronsToAdd;
    private int numberOfLayers;
    private int hiddenLayerWidth;
    private int numberOfOutputNodes;
    private int inputDataLength;
    private int inputRows;
    private int inputColumns;
    private Boolean shouldBuildNetwork;

    public int getNumberOfTrainingObjects() {
        return numberOfTrainingObjects;
    }

    public int getNumberOfEpochs() {
        return numberOfEpochs;
    }

    public double getLearnRate() {
        return learnRate;
    }

    public int getNumberOfLayers() {
        return numberOfLayers;
    }

    public int getHiddenLayerWidth() {
        return hiddenLayerWidth;
    }

    public int getNumberOfOutputNodes() {
        return numberOfOutputNodes;
    }

    public int getInputDataLength() {
        return inputDataLength;
    }

    public int getInputRows() {
        return inputRows;
    }

    public int getInputColumns() {
        return inputColumns;
    }

    public Boolean getShouldBuildNetwork() {
        return shouldBuildNetwork;
    }
}
