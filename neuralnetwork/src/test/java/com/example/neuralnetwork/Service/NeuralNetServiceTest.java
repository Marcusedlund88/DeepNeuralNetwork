package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.Math.StaticMathClass;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class NeuralNetServiceTest {

    @Mock
    private static MathOperations mathOperations;
    @Mock
    private static NeuralNetwork neuralNetwork;

    @Autowired
    private NeuralNetService neuralNetService;
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
        when(mockParam.getNumberOfLayers()).thenReturn(4);
        when(mockParam.getHiddenLayerWidth()).thenReturn(8);
        when(mockParam.getLearnRate()).thenReturn(0.05);
        when(mockParam.getNumberOfTrainingObjects()).thenReturn(10);
        when(mockParam.getNumberOfEpochs()).thenReturn(10);
        when(mockParam.getShouldBuildNetwork()).thenReturn(true);
    }

    @Test
    void verifyMatch() {
    }

    @Test
    void verifyMatchFail(){

    }

    @Test
    void startTraining(){
        neuralNetService.StartTraining(mockParam);
        assertTrue(neuralNetwork.getIsNetworkUp());
    }

    @Test
    void startTrainingFail() {
    }
}