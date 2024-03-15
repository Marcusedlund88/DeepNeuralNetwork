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

    /**
    * Creates an empty neural network with the specified number of layers, input and output neurons, and layer widths.
    * Initializes each layer with the appropriate type (InputLayer, HiddenLayer, OutputLayer) and sets up the neurons accordingly.
    * 
    * @param numberOfLayers The total number of layers in the network.
    * @param numberOfInputNeurons The number of neurons in the input layer.
    * @param numberOfOutputNodes The number of neurons in the output layer.
    * @param hiddenLayerWidth The width of each hidden layer (excluding bias neuron).
    * @param inputDataLength The length of the input data.
    */
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

    /**
    * Propagates input forward through the neural network layers,
    * activating neurons and computing predicted values.
    * Prints predicted value, expected value, and error percentage.
    */
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

    /**
    * Propagates error backward through the neural network layers,
    * updating weights based on gradient descent.
    * Calculates gradients for weights using backpropagation.
    */
    public void propagateBackwards(){
        dE_dA = StaticMathClass.dC_dA(predictedValue, expectedValue);

        double[] dA_dZ;
        double[][] dZ_dW;
        double[] dC_dW;
        double[][] dZ_dA;
        double[] cachedGradients;
        double[] tempGradients;
        double[] hiddenTempGradients;

        List<Layer> layerList = Arrays.asList(layers);
        Collections.reverse(layerList);
        Neuron[] neurons;
        Layer previousLayer = null;

        for(Layer layer : layerList){
            tempGradients = StaticMathClass.makeZeroVector(inputDataLength);
            hiddenTempGradients = StaticMathClass.makeZeroVector(inputDataLength);
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
                        dC_dW = StaticMathClass.dC_dW_hidden(cachedGradients, dA_dZ, dZ_dW);

                        tempGradients = StaticMathClass.vectorMatrixMultiplication(dA_dZ, dZ_dA);
                        tempGradients = StaticMathClass.vectorMultiplication(cachedGradients, tempGradients);
                        hiddenTempGradients = StaticMathClass.vectorAddition(hiddenTempGradients,tempGradients);

                        neuron.setWeights(StaticMathClass.getUpdatedWeights(
                                neuron.getWeights(), dC_dW, learnRate
                        ));
                    }
                }
            }
            switch (layer.layerType){
                case OutputLayer -> layer.setBackPropCache(tempGradients);
                case HiddenLayer -> layer.setBackPropCache(hiddenTempGradients);
            }
            previousLayer = layer;
        }
        Collections.reverse(layerList);
    }

    /**
    * Initializes neurons for the layer and sets initial weights.
    * For each neuron, creates a new instance with specified parameters,
    * sets bias, and sets initial weights.
    * If it's not the last neuron or the layer is an input or output layer,
    * creates a regular neuron; otherwise, creates a bias neuron.
    * Sets the initialized neurons to the layer.
    * If the layer is a hidden or output layer, sets initial weights for neurons.
    */
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

    /**
    * Sets initial weights for neurons using Xavier/Glorot initialization.
    * For each neuron in the given array, generates starting weights
    * using Xavier/Glorot initialization and sets them to the neuron.
    * 
    * @param neurons The array of neurons for which initial weights are set.
    * @param edgesIn The number of incoming edges to each neuron.
    */
    private void setInitialWeights(Neuron[] neurons, int edgesIn){
        for(Neuron neuron : neurons){
            neuron.setWeights(
                    StaticMathClass
                            .generateStartingWeights(
                                    edgesIn, numberOfOutputNodes, inputDataLength
                            ));}
    }

    /**
    * Activates neurons in the given layer using the provided input.
    * For each neuron in the layer, activates it using the input,
    * retrieves the activated output, and stores it in the activatedLayerOutput matrix.
    * Sets the activatedLayerOutput matrix to the layer.
    * 
    * @param layer The layer in which neurons are activated.
    * @param neurons The array of neurons to be activated.
    * @param input The input matrix used for activation.
    */
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

    /**
    Get the predicted value produced by the output layer.
    */
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

    /**
    * Inserts two hidden layers into the neural network architecture.
    * Creates new layers with specified width and type as hidden layers.
    * Sets neurons for each new layer using the provided parameters.
    * Updates the layers array to include the newly inserted layers.
    */
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
