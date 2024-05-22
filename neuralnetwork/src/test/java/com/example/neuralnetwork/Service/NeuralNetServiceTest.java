package com.example.neuralnetwork.Service;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class NeuralNetServiceTest {

    @Mock
    private static MathOperations mathOperations;
    @Mock
    private static NeuralNetwork neuralNetwork;
    @Mock
    private static NeuralNetService neuralNetService;
    @Mock
    private static MongoDBService mongoDBService;
    @Mock
    private static SerializationService serializationService;
    private TrainingParam mockParam;
    private InputObject inputObject;

    @BeforeAll
    static void setUp() {
        neuralNetService = new NeuralNetService(neuralNetwork, mathOperations, mongoDBService, serializationService);
    }

    @BeforeEach
    void setUpBeforeEach(){

        mockParam = mock(TrainingParam.class);
        when(mockParam.getInputCase()).thenReturn(TrainingParam.InputCase.CASE_TEN);
        when(mockParam.getNumberOfOutputNodes()).thenReturn(1);
        when(mockParam.getNumberOfLayers()).thenReturn(4);
        when(mockParam.getHiddenLayerWidth()).thenReturn(8);
        when(mockParam.getLearnRate()).thenReturn(0.05);
        when(mockParam.getShouldBuildNetwork()).thenReturn(true);

        double[] vector1 = {1,1,1,1,1};
        double[] vector2 = {0,0,0,0,0};

        inputObject = mock(InputObject.class);
        when(inputObject.getUserOneInput()).thenReturn(vector1);
        when(inputObject.getUserTwoInput()).thenReturn(vector2);
        when(inputObject.getUserOneId()).thenReturn("A");
        when(inputObject.getUserTwoId()).thenReturn("B");

    }

    @Test
    void verifyMatch() {
        String response = neuralNetService.verifyMatch(inputObject);
        assertNotEquals("Error occurred", response);
    }

    @Test
    void verifyMatchFail(){

    }

    @Test
    void startTraining() {

    }

    @Test
    void startTrainingFail() {
    }
}