package com.example.neuralnetwork.Controller;

import com.example.neuralnetwork.Data.InputObject;
import com.example.neuralnetwork.Data.RollbackRequest;
import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.PropagationException;
import com.example.neuralnetwork.Math.BigFiveCalculator;
import com.example.neuralnetwork.Service.NeuralNetService;
import com.example.neuralnetwork.Validation.CustomValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("api/v1/")
public class MatchController {

    private final NeuralNetService neuralNetService;

    public MatchController(NeuralNetService neuralNetService){
        this.neuralNetService = neuralNetService;
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok("Welcome");
    }

    //TODO: Access level all
    @GetMapping("/request")
    public ResponseEntity<String> SendRequest(@RequestBody InputObject inputObject){
        try {
            return ResponseEntity.ok(neuralNetService.verifyMatch(inputObject));
        }
        catch(Exception e){
            return ResponseEntity.ok("Ooops something went wrong");
        }
    }
    //TODO: Access level all
    @GetMapping("/getRealValue")
    public ResponseEntity<String> GetRealValue(@RequestBody InputObject inputObject){
        try {
            double[][] input = BigFiveCalculator.makeBigFiveForm(inputObject.getUserOneInput(), inputObject.getUserTwoInput());
            double answer = BigFiveCalculator.bigFiveCompability(input, 10);
            return ResponseEntity.ok(Double.toString(answer));
        }
        catch(Exception e){
            return ResponseEntity.ok("Ooops something went wrong");
        }
    }
}
