package priv.rdo.trade.validation.validators.option;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Option;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

import static priv.rdo.trade.model.input.OptionStyle.AMERICAN;

@Component
public class OptionAmericanDateValidator implements Validator<Option> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(OptionAmericanDateValidator.class);

    private static final String EXERCISE_DATE_FIELD_NAME = "excerciseStartDate";
    private static final String TRADE_DATE_FIELD_NAME = "tradeDate";
    private static final String EXPIRY_DATE_FIELD_NAME = "expiryDate";

    @Override
    public boolean shouldValidate(Trade input) {
        if (!(input instanceof Option)) {
            return false;
        }

        Option option = (Option) input;
        return AMERICAN.name().equals(option.getStyle());
    }

    @Override
    public ValidationResult validate(Option input) {
        LOG.entry(input);

        if (input.getExcerciseStartDate() == null || input.getTradeDate() == null || input.getExpiryDate() == null) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(EXERCISE_DATE_FIELD_NAME, TRADE_DATE_FIELD_NAME, EXPIRY_DATE_FIELD_NAME));
        }

        if (input.getExcerciseStartDate().isAfter(input.getTradeDate()) && input.getExcerciseStartDate().isBefore(input.getExpiryDate())) {
            return LOG.exit(ValidationResult.success());
        } else {
            return LOG.exit(ValidationResult.failure("the exerciseStartDate has to be after the tradeDate and before the expiryDate",
                    EXERCISE_DATE_FIELD_NAME, TRADE_DATE_FIELD_NAME, EXPIRY_DATE_FIELD_NAME));
        }
    }
}
