package priv.rdo.trade.validation;

import org.springframework.stereotype.Service;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TradeValidationService {
    private final ValidationManager validationManager;

    public TradeValidationService(ValidationManager validationManager) {
        this.validationManager = validationManager;
    }

    public List<ValidationResult> validate(Trade trade) {
        return findValidationErrors(validationManager.validate(trade));
    }

    List<ValidationResult> findValidationErrors(Stream<ValidationResult> results) {
        return results.filter(ValidationResult::isFailure).collect(Collectors.toList());
    }
}
