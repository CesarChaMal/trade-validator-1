package priv.rdo.trade.validation.validators.common;

import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Forward;
import priv.rdo.trade.model.input.Spot;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.validation.external.holidays.HolidayCheckService;

import java.time.LocalDate;

/*
 * for info about why this got created (its ugly, I'll admit it) please refer to OptionWorkingDateValidator
 */
@Component
public class SpotAndForwardWorkingDateValidator extends WorkingDateValidator<Trade> {
    private static final String VALUE_DATE_FIELD_NAME = "valueDate";

    public SpotAndForwardWorkingDateValidator(HolidayCheckService holidayCheckService) {
        super(holidayCheckService);
    }

    @Override
    protected String getValidatedDateName() {
        return VALUE_DATE_FIELD_NAME;
    }

    @Override
    protected LocalDate getValidatedDate(Trade input) {
        return input.getValueDate();
    }

    @Override
    public boolean shouldValidate(Trade input) {
        return input instanceof Spot || input instanceof Forward;
    }
}
