package com.example.neuralnetwork.NeuralNetwork;

import com.example.neuralnetwork.Math.MathOperations;
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

    private final MathOperations mathOperations;

    public Neuron(int edgesIn, int edgesOut, int inputLength, NeuronType neuronType, MathOperations mathOperations){
        this.edgesIn = edgesIn;
        this.edgesOut = edgesOut;
        this.weights = new double[inputLength][edgesIn];
        this.neuronType = neuronType;
        this.neuronStatus = NeuronStatus.DeActivated;
        this.mathOperations = mathOperations;
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
                setWeightedOutput(mathOperations.weightedSum(input, getWeights()));
                setActivatedOutput(mathOperations.ReluActivateNeuron(getWeightedOutput()));
            }
            case Output -> {
                setWeightedOutput(mathOperations.weightedSum(input, getWeights()));
                setActivatedOutput(mathOperations.outputSigmoidActivation(getWeightedOutput()));
            }
            case Bias -> {
                setWeightedOutput(weights[0]);
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
