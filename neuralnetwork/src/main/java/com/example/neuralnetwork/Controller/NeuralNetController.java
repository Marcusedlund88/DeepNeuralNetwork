package com.example.neuralnetwork.Controller;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Data.SearchRequest;
import com.example.neuralnetwork.Data.TrainingRequest;
import com.example.neuralnetwork.Service.NeuralNetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("api/v1")
public class NeuralNetController {

    private NeuralNetService neuralNetService;

    public NeuralNetController(NeuralNetService neuralNetService){
        this.neuralNetService = neuralNetService;
    }

    //TODO: Access level all
    @PostMapping("/request")
    public String SendRequest(@RequestBody SearchRequest searchRequest){

        neuralNetService.processRequest(searchRequest.getQueryArray());
        return "OK";
    }

    //TODO: Access level admin only
    @PostMapping("/train")
    public String SendTRainingRequest(@RequestBody InputObject inputObject){
        TrainingRequest trainingRequest = new TrainingRequest();
        trainingRequest.setInputObject(inputObject);
        neuralNetService.processTrainingRequest(inputObject);
        return "OK";
    }

}
