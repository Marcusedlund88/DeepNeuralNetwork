package com.example.neuralnetwork.Math;

import com.example.neuralnetwork.Training.TrainingObject;
import com.example.neuralnetwork.Training.TrainingStrategy;

public final class BigFiveCalculator {

    /**
     * Calculates the compatibility score between two vectors based on the Big Five personality traits:
     * Openness, Conscientiousness, Extroversion, Agreeableness, and Neuroticism.
     * Compatibility scores are assigned based on the similarity of corresponding traits.
     *
     * @param input     A 2D array representing two vectors of Big Five personality trait values.
     * @param roundedBy The number of decimal places to round the compatibility score.
     */
    public static double bigFiveCompability(double[][] input, double roundedBy) {

        if (input.length != 2 && input[0].length != 5) {
            double[][] newInput = new double[2][5];
            for (int i = 0; i < 5; i++) {
                newInput[0][i] = input[1][0];
            }
            for (int i = 0; i < 5; i++) {
                newInput[1][i] = input[i][0];
            }
            input = newInput;
        }

        double[] vector1 = input[0];
        double[] vector2 = input[1];
        double[] matchVector = new double[vector1.length];
        double sum = 1;
        double ratio;

        for (int i = 0; i < vector1.length; i++) {
            ratio = Math.sqrt(Math.pow(vector1[i] - vector2[i], 2));
            matchVector[i] = getMatchValue(i, ratio);
        }
        for (double value : matchVector) {
            sum *= value;
        }
        return Math.round(sum * roundedBy) / roundedBy;
    }

    private static double getMatchValue(int index, double ratio){
        switch (index){
            case 0: // Openness
            case 1: // Conscientiousness
            case 3: // Agreeableness
                if (ratio <= 0.2) return 1;
                if (ratio <= 0.4) return 0.75;
                if (ratio <= 0.7) return 0.50;
                return 0.25;
            case 2: // Extroversion
                if (ratio <= 0.3) return 1;
                if (ratio <= 0.6) return 0.75;
                if (ratio <= 0.8) return 0.50;
                return 0.25;
            case 4: // Neuroticism
                if (ratio <= 0.1) return 0.25;
                if (ratio <= 0.2) return 0.50;
                if (ratio <= 0.3) return 1;
                if (ratio <= 0.4) return 0.75;
                if (ratio <= 0.5) return 0.50;
                if (ratio <= 0.6) return 0.50;
                return 0.25;
        }
        return 0;
    }
}
