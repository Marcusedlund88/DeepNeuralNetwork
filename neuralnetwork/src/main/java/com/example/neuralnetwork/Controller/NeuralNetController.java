package com.example.neuralnetwork.Controller;

import com.example.neuralnetwork.Data.*;
import com.example.neuralnetwork.Service.NeuralNetService;
import com.example.neuralnetwork.Validation.TrainingParamValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("api/v1")
public class NeuralNetController {

    private final NeuralNetService neuralNetService;
    private final TrainingParamValidator trainingParamValidator;

    public NeuralNetController(NeuralNetService neuralNetService, TrainingParamValidator trainingParamValidator){
        this.neuralNetService = neuralNetService;
        this.trainingParamValidator = trainingParamValidator;
    }

    //TODO: Access level all
    @GetMapping("/request")
    public ResponseEntity<String> SendRequest(@RequestBody InputObject inputObject){

        return ResponseEntity.ok(neuralNetService.verifyMatch(inputObject));
    }

    //TODO: Access level admin only
    @GetMapping("/initTraining")
    public ResponseEntity<String> initTraining(@RequestBody @Validated TrainingParam trainingParam, BindingResult bindingResult) {

        trainingParamValidator.validate(trainingParam, bindingResult);
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return a response indicating failure
            return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors());
        }
        neuralNetService.StartTraining(trainingParam);
        return ResponseEntity.ok("OK");
    }
}
