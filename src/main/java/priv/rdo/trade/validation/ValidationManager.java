package priv.rdo.trade.validation;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

import java.util.Collection;
import java.util.stream.Stream;

@Component
public class ValidationManager {
    private static final XLogger LOG = XLoggerFactory.getXLogger(ValidationManager.class);

    private final ValidationChain validationChain;

    public ValidationManager(ApplicationContext ctx) {
        validationChain = new ValidationChain();

        addValidators(ctx);
    }

    private void addValidators(ApplicationContext ctx) {
        LOG.debug("Initializing validation manager...");

        Collection<Validator> validators = findValidators(ctx);
        LOG.debug("Found {} validators: {}", validators.size(), validators);

        validators.forEach(validationChain::addValidator);
        LOG.debug("Added validators to the validation chain");
    }

    private Collection<Validator> findValidators(ApplicationContext ctx) {
        return ctx.getBeansOfType(Validator.class).values();
    }

    public Stream<ValidationResult> validate(Trade trade) {
        LOG.entry(trade);
        return LOG.exit(validationChain.validate(trade));
    }
}
