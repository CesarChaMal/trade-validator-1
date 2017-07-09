package priv.rdo.trade.model.output;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import priv.rdo.trade.model.input.Trade;

import java.util.List;

@Getter
@ToString
public class TradeValidationResult {
    private final ValidationStatus validationStatus;
    private List<ValidationResult> errors;
    private final Trade validatedTrade;

    public TradeValidationResult(Trade validatedTrade) {
        this.validationStatus = ValidationStatus.SUCCESS;
        this.validatedTrade = validatedTrade;
    }

    public TradeValidationResult(Trade validatedTrade, List<ValidationResult> errors) {
        this.validationStatus = ValidationStatus.FAILURE;
        this.validatedTrade = validatedTrade;
        this.errors = errors;
    }

    public boolean containsErrors() {
        return CollectionUtils.isNotEmpty(errors);
    }
}
