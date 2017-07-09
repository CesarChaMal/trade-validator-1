package priv.rdo.trade.validation.validators.forward;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import priv.rdo.trade.model.input.Forward;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.validators.Validator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumSet;
import java.util.Set;

@Component
public class ForwardValueDateValidator implements Validator<Forward> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(ForwardValueDateValidator.class);

    private static final String VALUE_DATE_FIELD_NAME = "valueDate";

    private static final Set<Month> QUARTERS = EnumSet.of(Month.MARCH, Month.JUNE, Month.SEPTEMBER, Month.DECEMBER);
    private static final TemporalAdjuster THIRD_FRIDAY_OF_A_MONTH = TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.FRIDAY);

    @Override
    public boolean shouldValidate(Trade input) {
        return input instanceof Forward;
    }

    /**
     * with forward trade the value date should be one of four possible dates
     * a possible date is every 3rd Friday of each quarter (more info in readme.md)
     */
    @Override
    public ValidationResult validate(Forward input) {
        LOG.entry(input);

        if (input.getValueDate() == null) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(VALUE_DATE_FIELD_NAME));
        }

        if (isA3rdFridayOfAQuarter(input.getValueDate())) {
            return LOG.exit(ValidationResult.success());
        } else {
            return LOG.exit(ValidationResult.failure("the value date should be a 3rd Friday of a quarter", VALUE_DATE_FIELD_NAME));
        }
    }

    boolean isA3rdFridayOfAQuarter(LocalDate valueDate) {
        if (!QUARTERS.contains(valueDate.getMonth())){
            return false;
        }

        LocalDate thirdFriday = valueDate.with(THIRD_FRIDAY_OF_A_MONTH);

        return thirdFriday.equals(valueDate);
    }
}