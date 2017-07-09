package priv.rdo.trade.validation;

import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ValidationChain {
    private final List<Validator> validators;

    ValidationChain() {
        this.validators = new ArrayList<>();
    }

    ValidationChain addValidator(Validator<? extends Trade> validator) {
        validators.add(validator);
        return this;
    }

    public Stream<ValidationResult> validate(Trade trade) {
        return validators.stream()
                .filter(validator -> validator.shouldValidate(trade))
                .map(validator -> validator.validate(trade));
    }
}
