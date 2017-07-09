package priv.rdo.trade.validation.validators.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.input.TradeType;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

@Component
public class TradeTypeValidator implements Validator<Trade> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(TradeTypeValidator.class);

    private static final String TYPE_FIELD_NAME = "type";

    @Override
    public boolean shouldValidate(Trade input) {
        return true;
    }

    @Override
    public ValidationResult validate(Trade input) {
        LOG.entry(input);

        if (StringUtils.isBlank(input.getType())) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(TYPE_FIELD_NAME));
        }

        if (TradeType.values().contains(input.getType())) {
            return LOG.exit(ValidationResult.success());
        } else {
            return LOG.exit(ValidationResult.failure("Type has to be one of: " + TradeType.values(), TYPE_FIELD_NAME));
        }
    }
}