package priv.rdo.trade.validation.validators.common

import priv.rdo.trade.validation.external.holidays.HolidayCheckService
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class WorkingDateValidatorTest extends Specification {
    SpotAndForwardWorkingDateValidator sut

    void setup() {
        HolidayCheckService holidayCheckService = Mock {
            isHoliday(LocalDate.of(2017, 7, 7)) >> true
            isHoliday(_) >> false
        }
        sut = new SpotAndForwardWorkingDateValidator(holidayCheckService)
    }

    @Unroll
    def "#date is #not a weekend"() {
        expect:
            sut.isWeekend(date) == expectedResult

        where:
            date                      || expectedResult
            LocalDate.of(2017, 7, 7)  || false
            LocalDate.of(2017, 7, 8)  || true
            LocalDate.of(2016, 7, 9)  || true
            LocalDate.of(2017, 7, 10) || false

            not = expectedResult ? "" : "not"
    }

    @Unroll
    def "should #not indicate holidays for #date"() {
        expect:
            sut.isHoliday(date) == expectedResult

        where:
            date                     || expectedResult
            LocalDate.of(2017, 7, 7) || true
            LocalDate.of(2017, 7, 8) || false
            LocalDate.of(2016, 7, 6) || false
            LocalDate.of(2017, 5, 8) || false

            not = expectedResult ? "" : "not"
    }
}
