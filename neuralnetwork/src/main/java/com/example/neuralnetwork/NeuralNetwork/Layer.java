package com.example.neuralnetwork.NeuralNetwork;

import com.example.neuralnetwork.Math.StaticMathClass;

public class Layer {

    protected enum LayerType{
        InputLayer,
        HiddenLayer,
        OutputLayer
    }
    protected LayerType layerType;
    private double[][] layerInput;
    private final int numberOfNeurons;
    private Neuron[] neurons;
    private double[] bias;
    private double[][] layerOutput;
    private double [][] activatedLayerOutput;
    private double[][] backPropCache;
    private double[][] dZ_dA;

    public Layer(int numberOfNodes, int dataLength, LayerType layerType){
        this.numberOfNeurons = numberOfNodes;
        neurons = new Neuron[numberOfNodes];
        bias = new double[numberOfNodes];
        layerOutput = new double[numberOfNodes][dataLength];
        activatedLayerOutput = new double[numberOfNodes][dataLength];
        this.layerType = layerType;
        this.backPropCache = new double[numberOfNodes][dataLength];
        this.dZ_dA = new double[numberOfNodes][dataLength];
    }

    public int getNumberOfNeurons() {
        return numberOfNeurons;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public void setNeurons(Neuron[] neurons) {
        this.neurons = neurons;
    }

    public double[] getBias() {
        return bias;
    }

    public void setBias(double[] bias) {
        this.bias = bias;
    }

    public double[][] getActivatedLayerOutput() {
        return activatedLayerOutput;
    }

    public void setActivatedLayerOutput(double[][] activatedLayerOutput) {
        this.activatedLayerOutput = activatedLayerOutput;
    }

    public void setLayerInput(double[][] input){
        this.layerInput = StaticMathClass.transposeMatrix(input);
    }

}
