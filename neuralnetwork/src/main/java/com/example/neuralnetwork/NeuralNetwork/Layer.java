package com.example.neuralnetwork.NeuralNetwork;

import com.example.neuralnetwork.Math.MathOperations;
import com.google.gson.annotations.Expose;

public class Layer {


    protected enum LayerType{
        InputLayer,
        HiddenLayer,
        OutputLayer
    }
    @Expose
    protected LayerType layerType;
    @Expose
    private double[][] layerInput;
    @Expose
    private int numberOfNeurons;
    @Expose
    private Neuron[] neurons;
    @Expose
    private double[] bias;
    @Expose
    private double[][] layerOutput;
    @Expose
    private double [][] activatedLayerOutput;
    @Expose
    private double[] backPropCache;
    @Expose
    private double[][] dZ_dA;
    private Layer previousLayer;
    private Layer nextLayer;
    @Expose
    private int dataLength;
    private final MathOperations mathOperations;

    public Layer(int numberOfNodes, int dataLength, LayerType layerType, MathOperations mathOperations){
        this.numberOfNeurons = numberOfNodes;
        neurons = new Neuron[numberOfNodes];
        bias = new double[numberOfNodes];
        layerOutput = new double[numberOfNodes][dataLength];
        activatedLayerOutput = new double[numberOfNodes][dataLength];
        this.layerType = layerType;
        this.backPropCache = null;
        this.dZ_dA = new double[numberOfNodes][dataLength];
        this.mathOperations = mathOperations;
        this.dataLength = dataLength;
    }

    public int getNumberOfNeurons() {
        return numberOfNeurons;
    }

    public void setNumberOfNeurons(int numberOfNeurons) {
        this.numberOfNeurons = numberOfNeurons;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public void setNeurons(Neuron[] neurons) {
        this.neurons = neurons;
    }

    public double[] getBiasGradients() {
        return bias;
    }

    public void setBiasGradients(double[] bias) {
        this.bias = bias;
    }

    public double[] getBackPropCache() {
        return backPropCache;
    }

    public void setBackPropCache(double[] backPropCache) {
        this.backPropCache = backPropCache;
    }

    public double[][] getActivatedLayerOutput() {
        return activatedLayerOutput;
    }

    public void setActivatedLayerOutput(double[][] activatedLayerOutput) {
        this.activatedLayerOutput = activatedLayerOutput;
    }

    public void setLayerInput(double[][] input){
        this.layerInput = mathOperations.transposeMatrix(input);
    }

    public Layer getPreviousLayer() {
        return previousLayer;
    }

    public void setPreviousLayer(Layer previousLayer) {
        this.previousLayer = previousLayer;
    }

    public Layer getNextLayer() {
        return nextLayer;
    }

    public void setNextLayer(Layer nextLayer) {
        this.nextLayer = nextLayer;
    }
}
