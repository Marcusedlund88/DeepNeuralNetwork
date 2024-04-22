package com.example.neuralnetwork.Data;

import lombok.Data;

@Data
public class InputObject {
    private double[][] input;
    private int numberOfLayers;
    private int hiddenLayerWidth;
    private int numberOfOutputNodes;
    private double[][] expectedValue;
    private double learnRate;

    public InputObject(double[][] input, int numberOfLayers, int hiddenLayerWidth, int numberOfOutputNodes, double[][] expectedValue, double learnRate) {
        this.input = input;
        this.numberOfLayers = numberOfLayers;
        this.hiddenLayerWidth = hiddenLayerWidth;
        this.numberOfOutputNodes = numberOfOutputNodes;
        this.expectedValue = expectedValue;
        this.learnRate = learnRate;
    }

    public double[][] getInput() {
        return input;
    }

    public void setInput(double[][] input) {
        this.input = input;
    }

    public int getNumberOfLayers() {
        return numberOfLayers;
    }

    public void setNumberOfLayers(int numberOfLayers) {
        this.numberOfLayers = numberOfLayers;
    }

    public int getHiddenLayerWidth() {
        return hiddenLayerWidth;
    }

    public void setHiddenLayerWidth(int hiddenLayerWidth) {
        this.hiddenLayerWidth = hiddenLayerWidth;
    }

    public int getNumberOfOutputNodes() {
        return numberOfOutputNodes;
    }

    public void setNumberOfOutputNodes(int numberOfOutputNodes) {
        this.numberOfOutputNodes = numberOfOutputNodes;
    }

    public double[][] getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(double[][] expectedValue) {
        this.expectedValue = expectedValue;
    }

    public double getLearnRate() {
        return learnRate;
    }

    public void setLearnRate(double learnRate) {
        this.learnRate = learnRate;
    }
}
