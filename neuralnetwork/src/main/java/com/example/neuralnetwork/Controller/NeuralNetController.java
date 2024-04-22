package com.example.neuralnetwork.Controller;

import com.example.neuralnetwork.Data.SearchRequest;
import com.example.neuralnetwork.Service.NeuralNetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
@RequestMapping("api/v1")
public class NeuralNetController {

    private NeuralNetService neuralNetService;

    public NeuralNetController(NeuralNetService neuralNetService){
        this.neuralNetService = neuralNetService;
    }

    @PostMapping("/request")
    public String SendRequest(@RequestBody SearchRequest searchRequest){

        neuralNetService.processRequest(searchRequest.getQueryArray());
        return "OK";
    }

}
