package priv.rdo.trade.validation.validators.common;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.CurrencyPair;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

@Component
public class CurrencyPairValidator implements Validator<Trade> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(CurrencyPairValidator.class);

    private static final String CURRENCY_PAIR_FIELD_NAME = "ccyPair";

    @Override
    public boolean shouldValidate(Trade input) {
        return true;
    }

    @Override
    public ValidationResult validate(Trade input) {
        LOG.entry(input);

        CurrencyPair currencyPair = input.getCcyPair();
        if (currencyPair == null) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(CURRENCY_PAIR_FIELD_NAME));
        }

        if (currencyPair.getLeftCurrency() == null || currencyPair.getRightCurrency() == null) {
            return LOG.exit(ValidationResult.failure("currency value is invalid", CURRENCY_PAIR_FIELD_NAME));
        } else {
            return LOG.exit(ValidationResult.success());
        }
    }
}
