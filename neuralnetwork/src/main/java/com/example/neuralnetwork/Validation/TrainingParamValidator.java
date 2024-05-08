package com.example.neuralnetwork.Validation;


import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TrainingParamValidator implements Validator {

    private final Validator trainingParamValidator;

    public TrainingParamValidator(Validator trainingParamValidator){
        this.trainingParamValidator = trainingParamValidator;
    }

    @Override
    public boolean supports(Class clazz) {
        return TrainingParam.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        try {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "inputCase", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numberOfTrainingObjects", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numberOfEpochs", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "learnRate", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numberOfLayers", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hiddenLayerWidth", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numberOfOutputNodes", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shouldBuildNetwork", "field.required");

            TrainingParam trainingParam = (TrainingParam) target;

            if (!isValidTrainingRequest(trainingParam)){
                throw new ValidationException("poop");
            }
        }
        catch (Exception e){
            errors.reject("Input format error");
        }
    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }

    private boolean isValidTrainingRequest(TrainingParam trainingParam){
        return trainingParam != null &&
                (trainingParam.getInputCase() == TrainingParam.InputCase.CASE_FIVE|| trainingParam.getInputCase() == TrainingParam.InputCase.CASE_TEN) &&
                trainingParam.getNumberOfTrainingObjects() != 0 &&
                trainingParam.getNumberOfEpochs() != 0 &&
                trainingParam.getLearnRate() != 0 &&
                trainingParam.getNumberOfLayers() != 0 &&
                trainingParam.getHiddenLayerWidth() != 0 &&
                trainingParam.getNumberOfOutputNodes() != 0 &&
                trainingParam.getShouldBuildNetwork() != null;
    }
}
