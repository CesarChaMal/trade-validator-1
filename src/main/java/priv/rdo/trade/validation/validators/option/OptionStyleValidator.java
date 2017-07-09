package priv.rdo.trade.validation.validators.option;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Option;
import priv.rdo.trade.model.input.OptionStyle;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

import java.util.Arrays;

//this could be implemented using a DB or whatnot, but in that case it would be an overkill
@Component
public class OptionStyleValidator implements Validator<Option> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(OptionStyleValidator.class);

    private static final String STYLE_FIELD_NAME = "style";

    @Override
    public boolean shouldValidate(Trade input) {
        return input instanceof Option;
    }

    @Override
    public ValidationResult validate(Option input) {
        LOG.entry(input);

        if (input.getStyle() == null) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(STYLE_FIELD_NAME));
        }

        if (validStyle(input.getStyle())) {
            return LOG.exit(ValidationResult.success());
        } else {
            return LOG.exit(ValidationResult.failure("This field should be one of: " + Arrays.toString(OptionStyle.values()), STYLE_FIELD_NAME));
        }
    }

    boolean validStyle(String input) {
        return Arrays.stream(OptionStyle.values()).map(Enum::name).anyMatch(style -> style.equals(input));
    }

}