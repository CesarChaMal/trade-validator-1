package priv.rdo.trade.model.output;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class BulkTradeValidationResult {
    private List<TradeValidationResult> results;

    public BulkTradeValidationResult() {
        this.results = new ArrayList<>();
    }

    public void add(TradeValidationResult result) {
        results.add(result);
    }

    public boolean containsErrors() {
        return results.stream()
                .map(TradeValidationResult::containsErrors)
                .reduce(false, (identity, nextVal) -> identity || nextVal);
    }
}
