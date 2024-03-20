package com.example.neuralnetwork.NeuralNetwork;

import com.example.neuralnetwork.Math.StaticMathClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

        Layer previousLayer = null;
        Neuron[] neurons;
        int edgesIn = 0;
        for (Layer layer : layers){
            neurons = new Neuron[layer.getNumberOfNeurons()];

            switch (layer.layerType){
                case InputLayer -> setLayer(
                        layer,
                        previousLayer,
                        neurons,
                        edgesIn,
                        hiddenLayerWidth,
                        inputDataLength,
                        Neuron.NeuronType.Input);
                case HiddenLayer -> setLayer(
                            layer,
                            previousLayer,
                            neurons,
                            edgesIn,
                            hiddenLayerWidth,
                            inputDataLength,
                            Neuron.NeuronType.Hidden
                    );
                case OutputLayer -> setLayer(
                            layer,
                            previousLayer,
                            neurons,
                            edgesIn,
                            0,
                            inputDataLength,
                            Neuron.NeuronType.Output);
            }
            previousLayer = layer;
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
        double[] cachedGradients = null;
        double[] tempGradients;
        double[] hiddenTempGradients;

        List<Layer> layerList = Arrays.asList(layers);
        Collections.reverse(layerList);
        Neuron[] neurons;
        Layer nextLayer = null;

        for(Layer layer : layerList){
            tempGradients = null;
            neurons = layer.getNeurons();
            nextLayer = layer.getNextLayer();
            for (Neuron neuron : neurons){
                switch (neuron.neuronType) {
                    case Output, Hidden, Bias -> {
                        if(layer.layerType == Layer.LayerType.OutputLayer && neuron.neuronType == Neuron.NeuronType.Bias)break;

                        cachedGradients = nextLayer != null ? nextLayer.getBackPropCache() : layer.getBackPropCache();

                        dA_dZ = getActivationGradient(neuron);
                        dZ_dA = neuron.getWeights();
                        dZ_dW = neuron.getInput();
                        dC_dW = getLossWeightGradient(neuron, dE_dA, dA_dZ, dZ_dW, cachedGradients);

                        switch (neuron.neuronType){
                            case Output -> {
                                tempGradients = StaticMathClass.vectorScalarMultiplication(dE_dA,dA_dZ);
                                tempGradients = StaticMathClass.vectorMatrixMultiplication(tempGradients,dZ_dA);

                                if(cachedGradients == null){
                                    layer.setBackPropCache(tempGradients);
                                }
                                else{
                                    cachedGradients = StaticMathClass.vectorAddition(tempGradients,cachedGradients);
                                    layer.setBackPropCache(cachedGradients);
                                }
                            }
                            case Hidden, Bias ->{

                                tempGradients = StaticMathClass.elementWiseVectorMultiplication(cachedGradients, dA_dZ);
                                tempGradients = StaticMathClass.vectorMatrixMultiplication(tempGradients,dZ_dA);

                                cachedGradients = StaticMathClass.vectorAddition(tempGradients,cachedGradients);
                                layer.setBackPropCache(cachedGradients);
                            }
                        }

                        updateWeights(neuron, dC_dW, learnRate);
                    }
                }
            }

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
    private void setLayer(Layer currentLayer, Layer previousLayer, Neuron[] neurons, int edgesIn, int edgesOut, int inputLength, Neuron.NeuronType neuronType){

        for(int i = 0; i < neurons.length; i++){
            if(i != neurons.length-1
                    || currentLayer.layerType == Layer.LayerType.InputLayer
                    || currentLayer.layerType == Layer.LayerType.OutputLayer){
                neurons[i] = new Neuron(edgesIn, edgesOut, inputLength, neuronType);
                neurons[i].setBias(StaticMathClass.generateRandomBias(edgesIn, edgesOut));
                continue;
            }
            neurons[i] = new Neuron(edgesIn, edgesOut, inputLength, Neuron.NeuronType.Bias);
        }

        currentLayer.setNeurons(neurons);

        switch (currentLayer.layerType){
            case InputLayer -> {}
            case HiddenLayer, OutputLayer -> {
                currentLayer.setPreviousLayer(previousLayer);
                previousLayer.setNextLayer(currentLayer);
                setInitialWeights(neurons,edgesIn);
            }
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
    private void setInitialWeights(Neuron[] neurons, int edgesIn) {
        for (Neuron neuron : neurons) {
            Random random = new Random();

            double[][] generatedWeights = new double[edgesIn][inputDataLength];
            for (int i = 0; i < edgesIn; i++) {
                for (int j = 0; j < inputDataLength; j++) {
                    double range = (Math.sqrt(6) / (Math.sqrt(edgesIn + numberOfOutputNodes)));
                    double minValue = -range;
                    double maxValue = range;

                    generatedWeights[i][j] = minValue + (maxValue - minValue) * random.nextDouble();
                }
            }

            neuron.setWeights(generatedWeights);
        }
    }

    /**
     * Sets updated weights for neurons using w_new = w_old - dc/dw * learn rate (alpha).
     * Iterates across the empty updatedWeights array and sets updated value.
     *
     * @param neuron Current neuron.
     * @param dC_dW Loss/weight gradient.
     * @param learnRate Current learn rate (alpha)
     */
    private void updateWeights(Neuron neuron, double[] dC_dW, double learnRate){
        double[][] weights = neuron.getWeights();
        weights = StaticMathClass.transposeMatrix(weights);
        double[][] updatedWeights = new double[weights.length][weights[0].length];


        for (int i = 0; i < updatedWeights.length; i++) {
            for (int j = 0; j < updatedWeights[0].length; j++) {
                updatedWeights[i][j] = weights[i][j] - learnRate*dC_dW[j];
            }
        }
        neuron.setWeights(StaticMathClass.transposeMatrix(updatedWeights));
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
     * Returns the gradient for the activation dA/dZ for given neuron.
     * Handles the neuron based on its type, Output/Hidden/Bias,
     * and returns the gradient based on its activation function.
     *
     * @param neuron Current neuron.
     */
    private double[] getActivationGradient(Neuron neuron){
        double[] activatedOutput = neuron.getActivatedOutput();
        double[] activationGradient = null;

        switch(neuron.neuronType){
            case Output -> {

                activationGradient = new double[activatedOutput.length];

                for (int i = 0; i < activatedOutput.length; i++) {
                    activationGradient[i] = activatedOutput[i]*(1-activatedOutput[i]);
                }
            }

            case Hidden,Bias -> {

                activationGradient = new double[activatedOutput.length];

                for (int i = 0; i < activationGradient.length; i++) {
                    if(activatedOutput[i] <= 0){
                        activationGradient[i] = 0;
                        continue;
                    }
                    activationGradient[i] = 1;
                }
            }
        }
        return activationGradient;
    }

    /**
     * Returns the gradient for the loss with respect to the weights dC/dW for given neuron.
     * Handles the neuron based on its type, Output/Hidden/Bias,
     * and returns the gradient based on its activation function.
     *
     * @param neuron Current neuron.
     */
    private double[] getLossWeightGradient(Neuron neuron, double dE_dA, double[] dA_dZ, double[][] dZ_dW, double[] dC_dW_prev){
        double[] dC_dW = new double[dZ_dW.length];

        switch (neuron.neuronType){
            case Output -> {

                dC_dW = StaticMathClass.vectorScalarMultiplication(dE_dA,dA_dZ);
                dZ_dW = StaticMathClass.transposeMatrix(dZ_dW);
                dC_dW = StaticMathClass.vectorMatrixMultiplication(dC_dW, dZ_dW);

                return dC_dW;
            }
            case Hidden, Bias -> {
                dC_dW = StaticMathClass.elementWiseVectorMultiplication(dC_dW_prev,dA_dZ);
                dZ_dW = StaticMathClass.transposeMatrix(dZ_dW);
                dC_dW = StaticMathClass.vectorMatrixMultiplication(dC_dW, dZ_dW);

                return dC_dW;
            }
        }
        return dC_dW;
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
        Layer inputLayer = layers[0];

        setLayer(
                newFirstLayer,
                inputLayer,
                neurons,
                numberOfInputNeurons,
                hiddenLayerWidth,
                inputDataLength,
                Neuron.NeuronType.Hidden);

        setLayer(
                newSecondLayer,
                newFirstLayer,
                neurons,
                hiddenLayerWidth,
                hiddenLayerWidth,
                inputDataLength,
                Neuron.NeuronType.Hidden);


        tempLayer[0] = layers[0];
        tempLayer[1] = newFirstLayer;
        tempLayer[2] = newSecondLayer;
        layers[3].setPreviousLayer(newSecondLayer);

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
