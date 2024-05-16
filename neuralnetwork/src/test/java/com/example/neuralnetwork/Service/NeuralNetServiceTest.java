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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class NeuralNetServiceTest {

    @Mock
    private static MathOperations mathOperations;
    @Mock
    private static NeuralNetwork neuralNetwork;
    @MockBean
    private static NeuralNetService neuralNetService;
    @MockBean
    private static MongoTemplate mongoTemplate;
    private TrainingParam mockParam;

    @BeforeAll
    static void setUp() {
        neuralNetService = new NeuralNetService(neuralNetwork, mathOperations, mongoTemplate);
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
    }

    @Test
    void verifyMatch() {
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