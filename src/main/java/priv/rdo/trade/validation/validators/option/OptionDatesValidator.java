package priv.rdo.trade.validation.validators.option;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Option;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

@Component
public class OptionDatesValidator implements Validator<Option> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(OptionDatesValidator.class);

    private static final String PREMIUM_DATE_FIELD_NAME = "premiumDate";
    private static final String DELIVERY_DATE_FIELD_NAME = "deliveryDate";
    private static final String EXPIRY_DATE_FIELD_NAME = "expiryDate";


    @Override
    public boolean shouldValidate(Trade input) {
        return input instanceof Option;
    }

    @Override
    public ValidationResult validate(Option input) {
        LOG.entry(input);

        if (input.getPremiumDate() == null || input.getDeliveryDate() == null || input.getExpiryDate() == null) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(PREMIUM_DATE_FIELD_NAME, DELIVERY_DATE_FIELD_NAME, EXPIRY_DATE_FIELD_NAME));
        }

        if (input.getPremiumDate().isBefore(input.getDeliveryDate()) && input.getExpiryDate().isBefore(input.getDeliveryDate())) {
            return LOG.exit(ValidationResult.success());
        } else {
            return LOG.exit(ValidationResult.failure("the expiry date and the premium date shall be before the delivery date",
                    PREMIUM_DATE_FIELD_NAME, DELIVERY_DATE_FIELD_NAME, EXPIRY_DATE_FIELD_NAME));
        }
    }
}
