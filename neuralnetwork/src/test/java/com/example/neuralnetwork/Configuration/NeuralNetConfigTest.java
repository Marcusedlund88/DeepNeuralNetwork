package com.example.neuralnetwork.Configuration;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Math.MathOperations;
import com.example.neuralnetwork.Math.StaticMathClass;
import com.example.neuralnetwork.NeuralNetwork.NeuralNetwork;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NeuralNetConfigTest {

    @Autowired
    private MathOperations mathOperations;

    @Autowired
    private InputObject inputObject;

    @Autowired
    private NeuralNetwork neuralNetwork;

    @Test
    void mathOperations() {
        assertNotNull(mathOperations);
        assertTrue(mathOperations instanceof StaticMathClass);
    }

    @Test
    void neuralNetwork() {
        assertNotNull(neuralNetwork);
    }

    @Test
    void inputObject() {
        assertNull(inputObject);
    }
}