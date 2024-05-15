package com.example.neuralnetwork.Validation;

import com.example.neuralnetwork.Data.RollbackRequest;
import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("trainingParamValidator")
public class TrainingParamValidator extends CustomValidator<TrainingParam> {

    @Override
    public boolean supports(Class clazz) {
        return TrainingParam.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        try {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "inputCase", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "learnRate", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numberOfLayers", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hiddenLayerWidth", "field.required");
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
        return super.validateObject(target);
    }

    @Override
    protected Validator getValidator() {
        return this;
    }

    private boolean isValidTrainingRequest(TrainingParam trainingParam){
        if((trainingParam.getInputCase() == TrainingParam.InputCase.CASE_TEN && trainingParam.getHiddenLayerWidth() < 10) ||
                (trainingParam.getInputCase() == TrainingParam.InputCase.CASE_FIVE && trainingParam.getHiddenLayerWidth() < 5)){
            return false;
        }
        return trainingParam != null &&
                (trainingParam.getInputCase() == TrainingParam.InputCase.CASE_FIVE|| trainingParam.getInputCase() == TrainingParam.InputCase.CASE_TEN) &&
                trainingParam.getLearnRate() != 0 &&
                trainingParam.getNumberOfLayers() != 0 &&
                trainingParam.getHiddenLayerWidth() != 0 &&
                trainingParam.getShouldBuildNetwork() != null;
    }
}