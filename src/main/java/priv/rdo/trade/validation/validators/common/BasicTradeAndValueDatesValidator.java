package priv.rdo.trade.validation.validators.common;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Forward;
import priv.rdo.trade.model.input.Spot;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

import java.time.LocalDate;

/*
 * because value date does not exist in the input data for Options I moved this validator out from common validation chain (which does not fit requirements)
 * This may be a bad idea and is something that I'd ask about, but I'm writing this code during the weekend so there is no one to ask :)
 */
@Component
public class BasicTradeAndValueDatesValidator implements Validator<Trade> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(BasicTradeAndValueDatesValidator.class);

    private static final String TRADE_DATE_FIELD_NAME = "tradeDate";
    private static final String VALUE_DATE_FIELD_NAME = "valueDate";

    @Override
    public boolean shouldValidate(Trade input) {
        return input instanceof Spot || input instanceof Forward;
    }

    @Override
    public ValidationResult validate(Trade input) {
        LOG.entry(input);

        if (input.getTradeDate() == null || input.getValueDate() == null) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(VALUE_DATE_FIELD_NAME, TRADE_DATE_FIELD_NAME));
        }

        if (valueDateIsNotBeforeTradeDate(input.getValueDate(), input.getTradeDate())) {
            return LOG.exit(ValidationResult.success());
        } else {
            return LOG.exit(ValidationResult.failure("Value date cannot be before trade date", VALUE_DATE_FIELD_NAME, TRADE_DATE_FIELD_NAME));
        }
    }

    private boolean valueDateIsNotBeforeTradeDate(LocalDate valueDate, LocalDate tradeDate) {
        return valueDate.isAfter(tradeDate) || valueDate.isEqual(tradeDate);
    }

}
