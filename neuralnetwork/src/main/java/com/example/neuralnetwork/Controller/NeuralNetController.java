package com.example.neuralnetwork.Controller;

import com.example.neuralnetwork.Data.*;
import com.example.neuralnetwork.Service.NeuralNetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("api/v1")
public class NeuralNetController {

    private final NeuralNetService neuralNetService;

    public NeuralNetController(NeuralNetService neuralNetService){
        this.neuralNetService = neuralNetService;
    }

    //TODO: Access level all
    @PostMapping("/request")
    public ResponseEntity<String> SendRequest(@RequestBody InputObject inputObject){

        neuralNetService.verifyMatch(inputObject);
        return ResponseEntity.ok("OK");
    }

    //TODO: Access level admin only
    @GetMapping("/initTraining")
    public ResponseEntity<String> initTraining(@RequestBody TrainingParam trainingParam){
        if(trainingParam != null){
            neuralNetService.StartTraining(trainingParam);
            return ResponseEntity.ok("OK");
        }
        return ResponseEntity.ok("Failed");
    }
}
