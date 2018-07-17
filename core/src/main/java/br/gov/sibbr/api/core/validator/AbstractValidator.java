package br.gov.sibbr.api.core.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.lang.reflect.ParameterizedType;

/**
 * Provider base for create validator
 */
public abstract class AbstractValidator<T> implements Validator {

    protected AbstractValidator() {
    }

    private Class<T> getType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(this.getType());
    }

    @Override
    public void validate(Object target, Errors errors) {
        this.valid((T) target, errors);
    }

    public abstract void valid(T target, Errors errors);

}
