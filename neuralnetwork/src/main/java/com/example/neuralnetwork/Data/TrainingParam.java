package com.example.neuralnetwork.Data;

import lombok.Data;

@Data
public class TrainingParam {

    public enum InputCase{
        CASE_FIVE,
        CASE_TEN,
        CASE_REBUILD
    }

    private int rows;
    private int columns;
    private InputCase inputCase;
    private int numberOfTrainingObjects;
    private int numberOfEpochs;
    private double learnRate;
    private int numberOfNeuronsToAdd;
    private int numberOfLayers;
    private int hiddenLayerWidth;
    private int numberOfOutputNodes;
    private Boolean shouldBuildNetwork;
    private Boolean isNewBatch;

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

    public Boolean getShouldBuildNetwork() {
        return shouldBuildNetwork;
    }

    public InputCase getInputCase(){
        return inputCase;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setNumberOfOutputNodes(int numberOfOutputNodes) {
        this.numberOfOutputNodes = numberOfOutputNodes;
    }

    public void setNumberOfTrainingObjects(int numberOfTrainingObjects) {
        this.numberOfTrainingObjects = numberOfTrainingObjects;
    }

    public void setNumberOfEpochs(int numberOfEpochs) {
        this.numberOfEpochs = numberOfEpochs;
    }

    public void setNumberOfLayers(int numberOfLayers) {
        this.numberOfLayers = numberOfLayers;
    }

    public void setHiddenLayerWidth(int hiddenLayerWidth) {
        this.hiddenLayerWidth = hiddenLayerWidth;
    }

    public Boolean getIsNewBatch() {
        return isNewBatch;
    }

    public void setIsNewBatch(Boolean isNewBatch) {
        this.isNewBatch = isNewBatch;
    }
}
