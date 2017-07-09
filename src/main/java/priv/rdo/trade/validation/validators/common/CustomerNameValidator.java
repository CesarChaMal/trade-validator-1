package priv.rdo.trade.validation.validators.common;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Customer;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

import java.util.Arrays;

//this could be implemented using a DB or whatnot, but in that case it would be an overkill
@Component
public class CustomerNameValidator implements Validator<Trade> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(CustomerNameValidator.class);

    private static final String CUSTOMER_FIELD_NAME = "customer";

    @Override
    public boolean shouldValidate(Trade input) {
        return true;
    }

    @Override
    public ValidationResult validate(Trade input) {
        LOG.entry(input);

        if (input.getCustomer() == null) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(CUSTOMER_FIELD_NAME));
        }

        if (isSupportedCustomer(input.getCustomer())) {
            return LOG.exit(ValidationResult.success());
        } else {
            return LOG.exit(ValidationResult.failure("We do not support a customer named " + input.getCustomer(), CUSTOMER_FIELD_NAME));
        }
    }

    boolean isSupportedCustomer(String customer) {
        return Arrays.stream(Customer.values()).map(Enum::name).anyMatch(custName -> custName.equals(customer));
    }
}
