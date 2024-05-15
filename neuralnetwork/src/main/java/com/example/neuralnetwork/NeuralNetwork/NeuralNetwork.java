package com.example.neuralnetwork.NeuralNetwork;

import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Data.TrainingSession;
import com.example.neuralnetwork.Exceptions.CreateEmptyNetworkException;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.Service.NeuralNetService;
import com.example.neuralnetwork.Training.TrainingObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Component
public class NeuralNetwork{

    private double[][] input;
    private int numberOfLayers;
    private int hiddenLayerWidth;
    private int numberOfOutputNodes;
    private int inputDataLength;
    private int lastLayerIndex;
    private int numberOfInputNeurons;
    private double[][] expectedValue;
    private double[][] predictedValue;
    private double learnRate;
    private Layer[] layers;
    private double mse;
    private double cacheMse;
    private List<TrainingObject> trainingObjects;
    private Boolean shouldBuildNetwork;
    private Boolean isNetworkUp;
    private final MathOperations mathOperations;

    private TrainingParam.InputCase cachedInputCase;

    public NeuralNetwork(MathOperations mathOperations) {
        this.mathOperations = mathOperations;
    }

    private void buildNetwork(
            int numberOfLayers,
            int hiddenLayerWidth,
            int numberOfOutputNodes,
            int columns,
            int lastLayerIndex,
            int rows){

        this.numberOfLayers = numberOfLayers;
        this.hiddenLayerWidth = hiddenLayerWidth;
        this.numberOfOutputNodes = numberOfOutputNodes;

        this.inputDataLength = columns;
        this.lastLayerIndex = numberOfLayers - 1;
        this.numberOfInputNeurons = rows;
        createEmptyNetwork();
        isNetworkUp = true;
    }
    public void setNeuralNetwork(TrainingParam trainingParam) throws CreateEmptyNetworkException {

        try {
            verifyNewNetworkBuild(trainingParam);

            if (shouldBuildNetwork) {
                buildNetwork(
                        trainingParam.getNumberOfLayers(),
                        trainingParam.getHiddenLayerWidth(),
                        trainingParam.getNumberOfOutputNodes(),
                        trainingParam.getColumns(),
                        trainingParam.getNumberOfLayers(),
                        trainingParam.getRows());
            }
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            throw new CreateEmptyNetworkException("Error creating network: "+ e.getCause());
        }
    }

    public void rollbackNetwork(Layer[] layers) throws CreateEmptyNetworkException{
        if(layers != null){
            TrainingParam trainingParam = new TrainingParam();
            trainingParam.setNumberOfOutputNodes(layers[layers.length-1].getNumberOfNeurons());
            trainingParam.setRows(layers[0].getNumberOfNeurons());
            trainingParam.setColumns(1);
            trainingParam.setHiddenLayerWidth(layers[1].getNumberOfNeurons()-1);
            trainingParam.setNumberOfLayers(layers.length);
            this.shouldBuildNetwork = true;

            try {
                setNeuralNetwork(trainingParam);
                this.layers = layers;

                for(Layer layer : layers){
                    layer.setMathOperations(mathOperations);
                    for(Neuron neuron : layer.getNeurons()){
                        neuron.setMathOperations(mathOperations);
                    }
                }
            }
            catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                throw new CreateEmptyNetworkException("Error creating network: "+ e.getCause());
            }
        }
    }

    /**
    * Creates an empty neural network with the specified number of layers, input and output neurons, and layer widths.
    * Initializes each layer with the appropriate type (InputLayer, HiddenLayer, OutputLayer) and sets up the neurons accordingly.
    */
    private void createEmptyNetwork() {

        layers = new Layer[numberOfLayers];

        for (int i = 0; i < numberOfLayers; i++) {
            if (i == 0) {
                layers[i] = new Layer(numberOfInputNeurons, inputDataLength, Layer.LayerType.InputLayer, mathOperations);
            }
            if (i == numberOfLayers - 1) {
                layers[i] = new Layer(numberOfOutputNodes, inputDataLength, Layer.LayerType.OutputLayer, mathOperations);
            }
            if (i != 0 && i != numberOfLayers - 1) {
                layers[i] = new Layer(hiddenLayerWidth + 1, inputDataLength, Layer.LayerType.HiddenLayer, mathOperations);
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
    public void propagateForward() throws PropagationException{

        try {
            double[][] layerInput = mathOperations.transposeMatrix(input);
            for (Layer layer : layers) {
                Neuron[] neurons = layer.getNeurons();
                switch (layer.layerType) {
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

            if(expectedValue != null){
                System.out.println(expectedValue[0][0]);
                mse = Math.sqrt(Math.pow((predictedValue[0][0] - expectedValue[0][0]), 2));
            }

            double value = predictedValue[0][0];
            double roundedValue = Math.round(value * 10.0) / 10.0;
            System.out.println(roundedValue);
            System.out.println();
        }
        catch (Exception e){
            throw new PropagationException("An " + e.getCause() +  " occurred during forward propagation");
        }
    }

    /**
    * Propagates error backward through the neural network layers,
    * updating weights based on gradient descent.
    * Calculates gradients for weights using backpropagation.
    */
    public void propagateBackwards() throws PropagationException {

        try {
            double[][] dE_dA_matrix = mathOperations.dC_dA(predictedValue, expectedValue);
            double dE_dA = dE_dA_matrix[0][0];
            double[] dA_dZ;
            double[][] dZ_dW;
            double[] dC_dW;
            double[][] dZ_dA;
            double[] cachedGradients;

            List<Layer> layerList = Arrays.asList(layers);
            Collections.reverse(layerList);
            Neuron[] neurons;
            Layer nextLayer;

            for (Layer layer : layerList) {
                double[] tempGradients;
                neurons = layer.getNeurons();
                nextLayer = layer.getNextLayer();
                for (Neuron neuron : neurons) {
                    switch (neuron.neuronType) {
                        case Output, Hidden, Bias -> {
                            if (layer.layerType == Layer.LayerType.OutputLayer && neuron.neuronType == Neuron.NeuronType.Bias)
                                break;

                            cachedGradients = nextLayer != null ? nextLayer.getBackPropCache() : layer.getBackPropCache();

                            dA_dZ = getActivationGradient(neuron);
                            dZ_dA = neuron.getWeights();
                            dZ_dW = neuron.getInput();
                            dC_dW = getLossWeightGradient(neuron, dE_dA, dA_dZ, dZ_dW, cachedGradients);

                            switch (neuron.neuronType) {
                                case Output -> {
                                    tempGradients = mathOperations.vectorScalarMultiplication(dE_dA, dA_dZ);
                                    tempGradients = mathOperations.vectorMatrixMultiplication(tempGradients, dZ_dA);

                                    if (cachedGradients == null) {
                                        layer.setBackPropCache(tempGradients);
                                    } else {
                                        cachedGradients = mathOperations.vectorAddition(tempGradients, cachedGradients);
                                        layer.setBackPropCache(cachedGradients);
                                    }
                                }
                                case Hidden, Bias -> {

                                    tempGradients = mathOperations.elementWiseVectorMultiplication(cachedGradients, dA_dZ);
                                    tempGradients = mathOperations.vectorMatrixMultiplication(tempGradients, dZ_dA);

                                    cachedGradients = mathOperations.vectorAddition(tempGradients, cachedGradients);
                                    layer.setBackPropCache(cachedGradients);
                                }
                            }

                            updateWeights(neuron, dC_dW, learnRate);
                        }
                    }
                }
            }
            Collections.reverse(layerList);
            for (Layer layer : layers) {
                layer.setBackPropCache(null);
            }
        }
        catch (Exception e){
            throw new PropagationException("An " + e.getCause() +  " occurred during back propagation");
        }
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
                neurons[i] = new Neuron(edgesIn, edgesOut, inputLength, neuronType, mathOperations);
                continue;
            }
            neurons[i] = new Neuron(edgesIn, edgesOut, inputLength, Neuron.NeuronType.Bias, mathOperations);
        }

        currentLayer.setNeurons(neurons);

        switch (currentLayer.layerType){
            case InputLayer -> {}
            case HiddenLayer, OutputLayer -> {
                currentLayer.setPreviousLayer(previousLayer);
                previousLayer.setNextLayer(currentLayer);
                setInitialWeights(neurons,edgesIn, edgesOut);
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
    private void setInitialWeights(Neuron[] neurons, int edgesIn, int edgesOut) {
        for (Neuron neuron : neurons) {
            Random random = new Random();

            double[][] generatedWeights = new double[edgesIn][inputDataLength];
            for (int i = 0; i < edgesIn; i++) {
                for (int j = 0; j < inputDataLength; j++) {
                    double range = (Math.sqrt(6) / (Math.sqrt(edgesIn + edgesOut)));
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
        weights = mathOperations.transposeMatrix(weights);
        double[][] updatedWeights = new double[weights.length][weights[0].length];


        for (int i = 0; i < updatedWeights.length; i++) {
            for (int j = 0; j < updatedWeights[0].length; j++) {
                updatedWeights[i][j] = weights[i][j] - learnRate*dC_dW[j];
            }
        }
        neuron.setWeights(mathOperations.transposeMatrix(updatedWeights));
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
        layer.setActivatedLayerOutput(mathOperations.transposeMatrix(activatedLayerOutput));
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

                dC_dW = mathOperations.vectorScalarMultiplication(dE_dA,dA_dZ);
                dZ_dW = mathOperations.transposeMatrix(dZ_dW);
                dC_dW = mathOperations.vectorMatrixMultiplication(dC_dW, dZ_dW);

                return dC_dW;
            }
            case Hidden, Bias -> {
                dC_dW = mathOperations.elementWiseVectorMultiplication(dC_dW_prev,dA_dZ);
                dZ_dW = mathOperations.transposeMatrix(dZ_dW);
                dC_dW = mathOperations.vectorMatrixMultiplication(dC_dW, dZ_dW);

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
            counter++;
        }
        predictedValue = mathOperations.GetPrediction(outputSum);
        setPredictedValue(predictedValue);
    }

    public void setInput(double[][] input) {
        this.input = input;
    }

    public void setExpectedValue(double[][] expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void setLearnRate(double learnRate){this.learnRate = learnRate;}

    public double getMse(){
        return mse;
    }

    public void setCacheMse(double cacheMse){this.cacheMse = cacheMse;}

    public double getCacheMse(){return cacheMse;}

    public void setTrainingObjects(List<TrainingObject> trainingObjects){this.trainingObjects = trainingObjects;}

    public List<TrainingObject> getTrainingObjects(){return trainingObjects;}

    public Boolean getIsNetworkUp(){
        if(isNetworkUp == null){
            isNetworkUp = false;
            return false;
        }
        return isNetworkUp;
    }

    private void verifyNewNetworkBuild(TrainingParam trainingParam){

        if(layers == null){
            shouldBuildNetwork = true;
            return;
        }
        if(!trainingParam.getShouldBuildNetwork()){
            shouldBuildNetwork = false;
            return;
        }
        shouldBuildNetwork = true;
    }

    private void setPredictedValue(double[][] predictedValue){
        this.predictedValue = predictedValue;
    }

    public double getPredictedValuePercent(){
        return predictedValue[0][0]*100;
    }

    public Layer[] getLayers(){
        return layers;
    }

    public void setLayers(Layer[] layers){
        this.layers = layers;
    }

    public TrainingParam.InputCase getCachedInputCase() {
        return cachedInputCase;
    }

    public void setCachedInputCase(TrainingParam.InputCase cachedInputCase) {
        this.cachedInputCase = cachedInputCase;
    }
}
