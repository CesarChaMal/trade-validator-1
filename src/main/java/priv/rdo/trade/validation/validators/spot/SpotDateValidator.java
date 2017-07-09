package priv.rdo.trade.validation.validators.spot;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Spot;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

@Component
public class SpotDateValidator implements Validator<Spot> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(SpotDateValidator.class);

    private static final String TRADE_DATE_FIELD_NAME = "tradeDate";
    private static final String VALUE_DATE_FIELD_NAME = "valueDate";

    @Override
    public boolean shouldValidate(Trade input) {
        return input instanceof Spot;
    }

    /**
     * with spot trade the value date should be exactly two days after trade date (more info in readme.md)
     */
    @Override
    public ValidationResult validate(Spot input) {
        LOG.entry(input);

        if (input.getTradeDate() == null || input.getValueDate() == null) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(VALUE_DATE_FIELD_NAME, TRADE_DATE_FIELD_NAME));
        }

        if (input.getTradeDate().plusDays(2).equals(input.getValueDate())) {
            return LOG.exit(ValidationResult.success());
        } else {
            return LOG.exit(ValidationResult.failure("the value date should be exactly two days after trade date", VALUE_DATE_FIELD_NAME, TRADE_DATE_FIELD_NAME));
        }
    }
}
