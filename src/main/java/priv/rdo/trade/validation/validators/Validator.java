package priv.rdo.trade.validation.validators;

import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;

public interface Validator<T extends Trade> {
    boolean shouldValidate(Trade input);

    ValidationResult validate(T input);
}
