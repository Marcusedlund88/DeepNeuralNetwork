package com.example.neuralnetwork.NeuralNetwork;

import com.example.neuralnetwork.Math.StaticMathClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NeuralNetwork {

    private double[][] input;
    private final int numberOfLayers;
    private final int hiddenLayerWidth;
    private final int numberOfOutputNodes;
    private final int inputDataLength;
    private final int lastLayerIndex;
    private final int numberOfInputNeurons;
    private double dE_dA;
    private double expectedValue;
    private double predictedValue;
    private final double learnRate;
    private Layer[] layers;

    public NeuralNetwork(double[][] input, int numberOfLayers, int hiddenLayerWidth, int numberOfOutputNodes, double expectedValue, double learnRate) {
        this.input = input;
        this.numberOfLayers = numberOfLayers;
        this.hiddenLayerWidth = hiddenLayerWidth;
        this.numberOfOutputNodes = numberOfOutputNodes;
        this.expectedValue = expectedValue;
        this.learnRate = learnRate;
        this.inputDataLength = input[0].length;
        this.lastLayerIndex = numberOfLayers-1;
        this.numberOfInputNeurons = input.length;
    }

    public void createEmptyNetwork() {

        layers = new Layer[numberOfLayers];

        for (int i = 0; i < numberOfLayers; i++) {
            if (i == 0) {
                layers[i] = new Layer(numberOfInputNeurons, inputDataLength, Layer.LayerType.InputLayer);
            }
            if (i == numberOfLayers - 1) {
                layers[i] = new Layer(numberOfOutputNodes, inputDataLength, Layer.LayerType.OutputLayer);
            }
            if (i != 0 && i != numberOfLayers - 1) {
                layers[i] = new Layer(hiddenLayerWidth + 1, inputDataLength, Layer.LayerType.HiddenLayer);
            }
        }

        Neuron[] neurons;
        int edgesIn = 0;
        for (Layer layer : layers){
            neurons = new Neuron[layer.getNumberOfNeurons()];

            switch (layer.layerType){
                case InputLayer -> setLayer(
                        layer,
                        neurons,
                        edgesIn,
                        hiddenLayerWidth,
                        inputDataLength,
                        Neuron.NeuronType.Input);
                case HiddenLayer -> setLayer(
                            layer,
                            neurons,
                            edgesIn,
                            hiddenLayerWidth,
                            inputDataLength,
                            Neuron.NeuronType.Hidden
                    );
                case OutputLayer -> setLayer(
                            layer,
                            neurons,
                            edgesIn,
                            0,
                            inputDataLength,
                            Neuron.NeuronType.Output);
            }
            edgesIn = layer.getNumberOfNeurons();
        }
    }

    public void propagateForward() {
        double[][] layerInput = StaticMathClass.transposeMatrix(input);
        for (Layer layer : layers){
            Neuron[] neurons = layer.getNeurons();
            switch (layer.layerType){
                case InputLayer -> {
                    layer.setLayerInput(layerInput);
                    layer.setActivatedLayerOutput(layerInput);
                }
                case HiddenLayer -> {
                    layer.setLayerInput(layerInput);
                    activateNeurons(layer, neurons, layerInput);
                }
                case OutputLayer -> {
                    layer.setLayerInput(layerInput);
                    activateNeurons(layer, neurons, layerInput);
                    getPredictedValue(layer.getNeurons());
                }
            }
            layerInput = layer.getActivatedLayerOutput();
        }
        System.out.println("Predicted Value: "+ predictedValue);
        System.out.println("Expected Value: "+ expectedValue);
        double error = 100*(Math.abs(predictedValue-expectedValue))/(0.5*(predictedValue + expectedValue));
        System.out.println("Error of: "+ error + " percent");
        System.out.println();
    }

    public void propagateBackwards(){
        dE_dA = StaticMathClass.dC_dA(predictedValue, expectedValue);

        double[] dA_dZ;
        double[][] dZ_dW;
        double[] dC_dW;
        double[][] dZ_dA;
        double[] cachedGradients;
        double[] tempGradients;

        List<Layer> layerList = Arrays.asList(layers);
        Collections.reverse(layerList);
        Neuron[] neurons;
        Layer previousLayer = null;

        for(Layer layer : layerList){
            tempGradients = StaticMathClass.makeZeroVector(inputDataLength);
            neurons = layer.getNeurons();
            for (Neuron neuron : neurons){
                switch (neuron.neuronType) {
                    case Output -> {
                        dA_dZ = StaticMathClass.dA_dZ_sigmoid(neuron.getActivatedOutput());
                        dZ_dW = neuron.getInput();
                        dC_dW = StaticMathClass.dC_dW_Output(dE_dA, dA_dZ, dZ_dW);

                        cachedGradients = StaticMathClass.vectorScalarMultiplication(dE_dA, dA_dZ);
                        cachedGradients = StaticMathClass.vectorMatrixMultiplication(cachedGradients, neuron.getWeights());
                        tempGradients = StaticMathClass.vectorAddition(cachedGradients, tempGradients);


                        neuron.setWeights(StaticMathClass.getUpdatedWeights(
                                neuron.getWeights(), dC_dW, learnRate
                        ));
                    }
                    case Hidden,Bias -> {
                        if(layer.layerType == Layer.LayerType.OutputLayer)break;
                        cachedGradients = previousLayer != null ? previousLayer.getBackPropCache() : null;

                        dZ_dA = StaticMathClass.transposeMatrix(neuron.getWeights());
                        dA_dZ = StaticMathClass.dA_dZ_relu(neuron.getActivatedOutput());
                        dZ_dW = neuron.getInput();
                        dC_dW = StaticMathClass.dC_dW_hidden(cachedGradients, dA_dZ, dZ_dW, dZ_dA);

                        tempGradients = StaticMathClass.vectorAddition(cachedGradients, tempGradients);

                        neuron.setWeights(StaticMathClass.getUpdatedWeights(
                                neuron.getWeights(), dC_dW, learnRate
                        ));
                    }
                }
            }
            layer.setBackPropCache(tempGradients);
            previousLayer = layer;
        }
        Collections.reverse(layerList);
    }

    private void setLayer(Layer layer, Neuron[] neurons, int edgesIn, int edgesOut, int inputLength, Neuron.NeuronType neuronType){

        for(int i = 0; i < neurons.length; i++){
            if(i != neurons.length-1
                    || layer.layerType == Layer.LayerType.InputLayer
                    || layer.layerType == Layer.LayerType.OutputLayer){
                neurons[i] = new Neuron(edgesIn, edgesOut, inputLength, neuronType);
                neurons[i].setBias(StaticMathClass.generateRandomBias(edgesIn, edgesOut));
                continue;
            }
            neurons[i] = new Neuron(edgesIn, edgesOut, inputLength, Neuron.NeuronType.Bias);
        }

        layer.setNeurons(neurons);

        switch (layer.layerType){
            case InputLayer -> {}
            case HiddenLayer, OutputLayer -> setInitialWeights(neurons,edgesIn);
        }
    }

    private void setInitialWeights(Neuron[] neurons, int edgesIn){
        for(Neuron neuron : neurons){
            neuron.setWeights(
                    StaticMathClass
                            .generateStartingWeights(
                                    edgesIn, numberOfOutputNodes, inputDataLength
                            ));}
    }

    private void activateNeurons(Layer layer, Neuron[] neurons, double[][] input){
        double[][] activatedLayerOutput = new double[neurons.length][inputDataLength];
        int counter = 0;
        for(Neuron neuron : neurons){
            neuron.ActivateNeuron(input);
            activatedLayerOutput[counter] = neuron.getActivatedOutput();
            counter++;
        }
        layer.setActivatedLayerOutput(StaticMathClass.transposeMatrix(activatedLayerOutput));
    }

    private void getPredictedValue(Neuron[] neurons){
        double[][] outputSum = new double[numberOfOutputNodes][inputDataLength];
        int counter = 0;
        for(Neuron neuron : neurons){
            outputSum[counter] = neuron.getActivatedOutput();
        }
        predictedValue = StaticMathClass.GetPrediction(outputSum);
    }

    public void setInput(double[][] input) {
        this.input = input;
    }

    public void setExpectedValue(double expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void addHiddenLayerFirst(){

        Neuron[] neurons = new Neuron[hiddenLayerWidth];
        Layer[] tempLayer = new Layer[numberOfLayers+1];
        Layer newFirstLayer = new Layer(hiddenLayerWidth, inputDataLength, Layer.LayerType.HiddenLayer);
        Layer newSecondLayer = new Layer(hiddenLayerWidth, inputDataLength, Layer.LayerType.HiddenLayer);

        setLayer(
                newFirstLayer,
                neurons,
                numberOfInputNeurons,
                hiddenLayerWidth,
                inputDataLength,
                Neuron.NeuronType.Hidden);

        setLayer(
                newSecondLayer,
                neurons,
                hiddenLayerWidth,
                hiddenLayerWidth,
                inputDataLength,
                Neuron.NeuronType.Hidden);


        tempLayer[0] = layers[0];
        tempLayer[1] = newFirstLayer;
        tempLayer[2] = newSecondLayer;

        for(int i = 2; i < layers.length; i++){
            tempLayer[i+1] = layers[i];
        }
        layers = tempLayer;
        System.out.println();
    }

    public void addLayerMiddle(){
        //TODO: To be implemented
    }

    public void addLayerLast(){
        //TODO: To be implemented
    }
}
