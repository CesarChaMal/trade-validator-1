package priv.rdo.trade.model.input;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Currency;

@Getter
@EqualsAndHashCode
public class CurrencyPair {
    private Currency leftCurrency;
    private Currency rightCurrency;

    public CurrencyPair(String currencyPair) {
        if (StringUtils.isNotBlank(currencyPair)) {
            leftCurrency = findCurrency(currencyPair.substring(0, 3));
            rightCurrency = findCurrency(currencyPair.substring(3));
        }
    }

    private Currency findCurrency(String currencyString) {
        try {
            return Currency.getInstance(currencyString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    @JsonValue
    public String toString() {
        return currencyCode(leftCurrency) + currencyCode(rightCurrency);
    }

    private static String currencyCode(Currency currency) {
        if (currency == null) {
            return "N/A";
        }

        return currency.getCurrencyCode();
    }
}
