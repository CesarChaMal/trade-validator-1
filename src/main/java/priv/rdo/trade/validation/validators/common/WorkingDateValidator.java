package priv.rdo.trade.validation.validators.common;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.external.holidays.HolidayCheckService;
import priv.rdo.trade.validation.external.holidays.HolidayCheckServiceNotWorkingException;
import priv.rdo.trade.validation.validators.Validator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

public abstract class WorkingDateValidator<T extends Trade> implements Validator<T> {
    private static final XLogger LOG = XLoggerFactory.getXLogger(WorkingDateValidator.class);

    private static final Set<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private final HolidayCheckService holidayCheckService;

    public WorkingDateValidator(HolidayCheckService holidayCheckService) {
        this.holidayCheckService = holidayCheckService;
    }

    @Override
    public ValidationResult validate(T input) {
        LOG.entry(input);

        if (getValidatedDate(input) == null) {
            return LOG.exit(ValidationResult.failureFieldsMandatory(getValidatedDateName()));
        }

        if (isWeekend(getValidatedDate(input))) {
            return LOG.exit(ValidationResult.failure("Date cannot occur during weekends", getValidatedDateName()));
        }

        try {
            if (isHoliday(getValidatedDate(input))) {
                return LOG.exit(ValidationResult.failure("Date cannot occur during bank holidays", getValidatedDateName()));
            } else {
                return LOG.exit(ValidationResult.success());
            }
        } catch (HolidayCheckServiceNotWorkingException e) {
            return LOG.exit(ValidationResult.failure("Working date validity could not be checked due to an external server error: " + e.getMessage(), getValidatedDateName()));
        }
    }

    protected abstract String getValidatedDateName();

    protected abstract LocalDate getValidatedDate(T input);

    boolean isWeekend(LocalDate date) {
        return WEEKEND.contains(date.getDayOfWeek());
    }

    boolean isHoliday(LocalDate date) {
        return holidayCheckService.isHoliday(date);
    }
}
