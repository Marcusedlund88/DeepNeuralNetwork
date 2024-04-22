package com.example.neuralnetwork.Data;

import lombok.Data;

@Data
public class SearchRequest {
    private double[][] queryArray;

    public double[][] getQueryArray(){
        return queryArray;
    }
}
