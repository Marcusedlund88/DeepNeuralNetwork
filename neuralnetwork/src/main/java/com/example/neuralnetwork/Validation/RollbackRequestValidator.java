package com.example.neuralnetwork.Validation;

import com.example.neuralnetwork.Data.RollbackRequest;
import com.example.neuralnetwork.Data.TrainingParam;
import com.example.neuralnetwork.Exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("rollbackRequestValidator")
public class RollbackRequestValidator extends CustomValidator<RollbackRequest> {

    @Override
    public boolean supports(Class clazz) {
        return RollbackRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        try {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "collection", "field.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "field.required");

            RollbackRequest rollbackRequest = (RollbackRequest) target;

            if (!isValidRollbackRequest(rollbackRequest)){
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

    private boolean isValidRollbackRequest(RollbackRequest rollbackRequest){

        return rollbackRequest != null &&
                rollbackRequest.getCollection() != null &&
                rollbackRequest.getId() != null;
    }
}
