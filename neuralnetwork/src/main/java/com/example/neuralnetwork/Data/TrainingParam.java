package com.example.neuralnetwork.Data;

import lombok.Data;

@Data
public class TrainingParam {

    public enum InputCase{
        CASE_FIVE,
        CASE_TEN,
        CASE_REBUILD
    }

    private InputCase inputCase;
    private int numberOfTrainingObjects;
    private int numberOfEpochs;
    private double learnRate;
    private int numberOfNeuronsToAdd;
    private int numberOfLayers;
    private int hiddenLayerWidth;
    private int numberOfOutputNodes;
    private int inputDataLength;
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

    public void setInputDataLength(int inputDataLength){
        this.inputDataLength = inputDataLength;
    }

    public Boolean getShouldBuildNetwork() {
        return shouldBuildNetwork;
    }

    public InputCase getInputCase(){
        return inputCase;
    }
}
