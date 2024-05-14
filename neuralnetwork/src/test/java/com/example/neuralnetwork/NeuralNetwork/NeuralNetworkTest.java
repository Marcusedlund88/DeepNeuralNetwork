package com.example.neuralnetwork.NeuralNetwork;

import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.CreateEmptyNetworkException;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.Math.StaticMathClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NeuralNetworkTest {

    @Mock
    private static MathOperations mathOperations;
    @Mock
    private static NeuralNetwork neuralNetwork;

    private TrainingParam mockParam;

    @BeforeAll
    static void setUp() {

        mathOperations = new StaticMathClass();

        neuralNetwork = new NeuralNetwork(mathOperations);
    }

    @BeforeEach
    void setUpBeforeEach(){

        mockParam = mock(TrainingParam.class);

        when(mockParam.getNumberOfOutputNodes()).thenReturn(1);
        when(mockParam.getInputCase()).thenReturn(TrainingParam.InputCase.CASE_TEN);
        when(mockParam.getNumberOfLayers()).thenReturn(4);
        when(mockParam.getHiddenLayerWidth()).thenReturn(8);
        when(mockParam.getLearnRate()).thenReturn(0.05);
        when(mockParam.getNumberOfTrainingObjects()).thenReturn(10);
        when(mockParam.getNumberOfEpochs()).thenReturn(10);
        when(mockParam.getShouldBuildNetwork()).thenReturn(true);
    }

    @Test
    void setNeuralNetwork() {

        try {
            neuralNetwork.setNeuralNetwork(mockParam);

            assertTrue(neuralNetwork.getIsNetworkUp());
        }
        catch(CreateEmptyNetworkException e){
            System.out.println("Exception caught");
        }
    }

    @Test
    void propagateForward() {
        try {
            neuralNetwork.setNeuralNetwork(mockParam);

            neuralNetwork.propagateForward();

            assertNotNull(neuralNetwork.getPredictedValuePercent());
        }
        catch(CreateEmptyNetworkException | PropagationException e){
            System.out.println("Exception caught");
        }
    }

    @Test
    void propagateForwardError() {
        when(mockParam.getNumberOfOutputNodes()).thenReturn(0);
        when(mockParam.getNumberOfLayers()).thenReturn(0);
        when(mockParam.getHiddenLayerWidth()).thenReturn(0);
        when(mockParam.getLearnRate()).thenReturn(0.05);
        when(mockParam.getNumberOfTrainingObjects()).thenReturn(10);
        when(mockParam.getNumberOfEpochs()).thenReturn(10);
        when(mockParam.getShouldBuildNetwork()).thenReturn(null);

        try {
            neuralNetwork.setNeuralNetwork(mockParam);

            Throwable exception = assertThrows(PropagationException.class, () -> {
                neuralNetwork.propagateForward();
            });

            assertNotNull(exception);
        }
        catch(CreateEmptyNetworkException e){
            System.out.println("Exception caught");
        }
    }

    @Test
    void propagateBackwards() {
        try {
            neuralNetwork.setNeuralNetwork(mockParam);

            neuralNetwork.propagateForward();
            double beforeBackProp = neuralNetwork.getPredictedValuePercent();
            neuralNetwork.propagateBackwards();
            neuralNetwork.propagateForward();
            double afterBackProp = neuralNetwork.getPredictedValuePercent();

            assertNotEquals(beforeBackProp, afterBackProp);
        }
        catch(CreateEmptyNetworkException | PropagationException e){
            System.out.println("Exception caught");
        }
    }

    @Test
    void propagateBackwardsError(){
        try {
            neuralNetwork.setNeuralNetwork(mockParam);

            Throwable exception = assertThrows(PropagationException.class, () -> {
                neuralNetwork.propagateBackwards();
            });

            assertNotNull(exception);
        }
        catch(CreateEmptyNetworkException e){
            System.out.println("Exception caught");
        }
    }
}