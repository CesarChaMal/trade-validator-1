package priv.rdo.trade.validation.validators.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

import java.util.Arrays;
import java.util.List;

//this could be implemented using a DB or whatnot, but in that case it would be an overkill
@Component
public class LegalEntityValidator implements Validator<Trade> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(LegalEntityValidator.class);

    private static final String LEGAL_ENTITY_FIELD_NAME = "legalEntity";

    private static final List<String> ALLOWED_LEGAL_ENTITIES = Arrays.asList("CS Zurich");

    @Override
    public boolean shouldValidate(Trade input) {
        return true;
    }

    @Override
    public ValidationResult validate(Trade input) {
        LOG.entry(input);

        if (StringUtils.isBlank(input.getLegalEntity())) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(LEGAL_ENTITY_FIELD_NAME));
        }

        if (isSupportedEntity(input.getLegalEntity())) {
            return LOG.exit(ValidationResult.success());
        } else {
            return LOG.exit(ValidationResult.failure("We do not support an named " + input.getLegalEntity(), LEGAL_ENTITY_FIELD_NAME));
        }
    }

    boolean isSupportedEntity(String legalEntity) {
        return ALLOWED_LEGAL_ENTITIES.contains(legalEntity);
    }
}
