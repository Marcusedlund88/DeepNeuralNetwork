package com.example.neuralnetwork.NeuralNetwork;

import com.example.neuralnetwork.Math.StaticMathClass;

public class Neuron {

    protected enum NeuronType{
        Input,
        Hidden,
        Output,
        Bias,
    }

    protected enum NeuronStatus{
        Activated,
        DeActivated
    }
    protected NeuronType neuronType;
    private NeuronStatus neuronStatus;
    private int edgesIn;
    private int edgesOut;
    private double[][] input;
    private double[][] weights;
    private double[] weightedOutput;
    private double[] activatedOutput;
    private double bias;

    public Neuron(int edgesIn, int edgesOut, int inputLength, NeuronType neuronType){
        this.edgesIn = edgesIn;
        this.edgesOut = edgesOut;
        this.weights = new double[inputLength][edgesIn];
        this.neuronType = neuronType;
        this.neuronStatus = NeuronStatus.DeActivated;
    }

    public double[][] getInput() {
        return input;
    }

    public void ActivateNeuron(double[][] input) {

        neuronStatus = NeuronStatus.Activated;
        this.input = input;
        switch (neuronType){
            case Input -> setActivatedOutput(input[0]);
            case Hidden -> {
                setWeightedOutput(StaticMathClass.weightedSum(input, getWeights()));
                setActivatedOutput(StaticMathClass.ReluActivateNeuron(getWeightedOutput()));
            }
            case Output -> {
                setWeightedOutput(StaticMathClass.weightedSum(input, getWeights()));
                setActivatedOutput(StaticMathClass.outputSigmoidActivation(getWeightedOutput()));
            }
            case Bias -> {
                input = StaticMathClass.fillMatrixWithSameValue(input.length, input[0].length, 1);
                setWeightedOutput(StaticMathClass.weightedSum(input, getWeights()));
                setActivatedOutput(getWeightedOutput());
            }
        }
    }

    public double[][] getWeights() {
        return weights;
    }

    public void setWeights(double[][] weights) {
        this.weights = weights;
    }

    public double[] getWeightedOutput() {
        return weightedOutput;
    }

    public void setWeightedOutput(double[] weightedOutput) {
        this.weightedOutput = weightedOutput;
    }

    public double[] getActivatedOutput() {
        return activatedOutput;
    }

    public void setActivatedOutput(double[] activatedOutput) {
        this.activatedOutput = activatedOutput;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
