package priv.rdo.trade.validation.validators.option;

import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Option;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.validation.external.holidays.HolidayCheckService;
import priv.rdo.trade.validation.validators.common.WorkingDateValidator;

import java.time.LocalDate;

/*
 * because value date does not exist in the input data for Options I moved this validator out from common validation chain (which does not fit requirements) and split it in two
 * I just assumed that some other date has to be checked for being working date in case of option
 * This may be a bad idea and is something that I'd ask about, but I'm writing this code during the weekend so there is no one to ask :)
 */
@Component
public class OptionWorkingDateValidator extends WorkingDateValidator<Option> {
    private static final String DELIVERY_DATE_FIELD_NAME = "deliveryDate";

    public OptionWorkingDateValidator(HolidayCheckService holidayCheckService) {
        super(holidayCheckService);
    }

    @Override
    protected String getValidatedDateName() {
        return DELIVERY_DATE_FIELD_NAME;
    }

    @Override
    protected LocalDate getValidatedDate(Option input) {
        return input.getDeliveryDate();
    }

    @Override
    public boolean shouldValidate(Trade input) {
        return input instanceof Option;
    }
}
