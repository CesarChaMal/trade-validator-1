package priv.rdo.trade.validation.validators.option

import priv.rdo.trade.model.input.Forward
import priv.rdo.trade.model.input.Option
import priv.rdo.trade.model.input.Spot
import priv.rdo.trade.model.input.Trade
import priv.rdo.trade.model.output.ValidationResult
import priv.rdo.trade.validation.external.holidays.HolidayCheckService
import priv.rdo.trade.validation.external.holidays.HolidayCheckServiceNotWorkingException
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class OptionWorkingDateValidatorTest extends Specification {

    @Unroll
    def "should #not validate #input"() {
        given:
            OptionWorkingDateValidator sut = new OptionWorkingDateValidator(null)
        expect:
            sut.shouldValidate(input) == expectedResult

        where:
            input         || expectedResult
            new Spot()    || false
            new Option()  || true
            new Trade()   || false
            new Forward() || false
            null          || false

            not = expectedResult ? "" : "not"
    }

    @Unroll
    def "should #not return validation error(#desc) for #input"() {
        given:
            HolidayCheckService holidayCheckService = Mock(HolidayCheckService) {
                isHoliday(LocalDate.of(2017, 7, 7)) >> true
                isHoliday(_) >> false
            }
            OptionWorkingDateValidator sut = new OptionWorkingDateValidator(holidayCheckService)

        when:
            ValidationResult result = sut.validate(testOption(input))

        then:
            result.isSuccess() == expectedSuccess
            result.getMessage() == expectedMessage

        where:
            input                     || expectedSuccess | expectedMessage
            LocalDate.of(2017, 7, 7)  || false           | "Date cannot occur during bank holidays"
            LocalDate.of(2017, 7, 8)  || false           | "Date cannot occur during weekends"
            LocalDate.of(2017, 7, 9)  || false           | "Date cannot occur during weekends"
            LocalDate.of(2017, 7, 10) || true            | null

            not = expectedSuccess ? "not" : ""
            desc = expectedSuccess ? "" : expectedMessage
    }

    def "should return validation error if service broke down"() {
        given:
            HolidayCheckService holidayCheckService = Mock(HolidayCheckService) {
                isHoliday(_) >> {input -> throw new HolidayCheckServiceNotWorkingException("testMessage")}
            }
            OptionWorkingDateValidator sut = new OptionWorkingDateValidator(holidayCheckService)

        when:
            ValidationResult result = sut.validate(testOption(LocalDate.of(2017, 7, 7)))

        then:
            result.isFailure()
            result.getMessage().contains("external server error")
            result.getMessage().contains("testMessage")
    }

    Option testOption(LocalDate date) {
        Option trade = new Option()

        trade.deliveryDate = date

        trade
    }
}
