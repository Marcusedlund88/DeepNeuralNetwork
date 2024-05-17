package com.example.neuralnetwork.Controller;

import com.example.neuralnetwork.Data.*;
import com.example.neuralnetwork.Service.NeuralNetService;
import com.example.neuralnetwork.Validation.CustomValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("api/v1")
public class NeuralNetController {

    private final NeuralNetService neuralNetService;
    //private final MongoTemplate mongoTemplate;

    private final CustomValidator<TrainingParam> trainingParamValidator;
    private final CustomValidator<RollbackRequest> rollbackRequestValidator;

    public NeuralNetController(NeuralNetService neuralNetService,
                               @Qualifier("trainingParamValidator") CustomValidator<TrainingParam> trainingParamValidator,
                               @Qualifier("rollbackRequestValidator") CustomValidator<RollbackRequest> rollbackRequestValidator,
                               MongoTemplate mongoTemplate){

        this.neuralNetService = neuralNetService;
        this.trainingParamValidator = trainingParamValidator;
        this.rollbackRequestValidator = rollbackRequestValidator;
        //this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok("Welcome");
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
        neuralNetService.startTraining(trainingParam);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/loadNetwork")
    public ResponseEntity<String> loadNetwork(@RequestBody @Validated RollbackRequest rollbackRequest, BindingResult bindingResult){

        rollbackRequestValidator.validate(rollbackRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return a response indicating failure
            return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors());
        }
        String response = neuralNetService.loadNetwork(rollbackRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping ResponseEntity<String> requestWithoutNeuralNet(){

        return ResponseEntity.ok("OK");
    }
}
