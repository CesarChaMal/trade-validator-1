package priv.rdo.trade.validation.validators.option

import priv.rdo.trade.model.input.Forward
import priv.rdo.trade.model.input.Option
import priv.rdo.trade.model.input.Spot
import priv.rdo.trade.model.input.Trade
import priv.rdo.trade.model.output.ValidationResult
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

import static priv.rdo.trade.model.input.OptionStyle.AMERICAN
import static priv.rdo.trade.model.input.OptionStyle.EUROPEAN

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class OptionAmericanDateValidatorTest extends Specification {
    OptionAmericanDateValidator sut = new OptionAmericanDateValidator()

    @Unroll
    def "should #not try to validate #input"() {
        expect:
            sut.shouldValidate(input) == expectedResult

        where:
            input                            || expectedResult
            optionWithStyle(AMERICAN.name()) || true
            optionWithStyle(EUROPEAN.name()) || false
            optionWithStyle("asdasd")        || false
            optionWithStyle("")              || false
            optionWithStyle(null)            || false
            new Spot()                       || false
            new Option()                     || false
            new Trade()                      || false
            new Forward()                    || false
            null                             || false

            not = expectedResult ? "" : "not"
    }

    Option optionWithStyle(String style) {
        Option option = new Option()

        option.style = style

        option
    }

    @Unroll
    def "should return #expectedMessage after validation for exercise='#exercise', trade='#trade', expiry='#expiry'"() {
        when:
            ValidationResult result = sut.validate(testOption(exercise, trade, expiry))

        then:
            !result.success
            result.message.contains(expectedMessage)

        where:
            exercise | trade   | expiry  || expectedMessage
            null     | date(8) | date(9) || "fields are mandatory"
            date(7)  | null    | date(9) || "fields are mandatory"
            date(7)  | date(8) | null    || "fields are mandatory"
            date(7)  | date(7) | date(9) || "the exerciseStartDate has to be after the tradeDate and before the expiryDate"
            date(7)  | date(6) | date(7) || "the exerciseStartDate has to be after the tradeDate and before the expiryDate"
            date(9)  | date(6) | date(7) || "the exerciseStartDate has to be after the tradeDate and before the expiryDate"
            date(3)  | date(6) | date(7) || "the exerciseStartDate has to be after the tradeDate and before the expiryDate"
    }

    @Unroll
    def "should positively validate exercise='#exercise', trade='#trade', expiry='#expiry'"() {
        when:
            ValidationResult result = sut.validate(testOption(exercise, trade, expiry))

        then:
            result.success
            result.message == null

        where:
            exercise | trade   | expiry
            date(7)  | date(6) | date(8)
            date(7)  | date(3) | date(15)
    }

    def date(int day) {
        LocalDate.of(2017, 7, day)
    }

    Option testOption(LocalDate exercise, LocalDate trade, LocalDate expiry) {
        Option option = new Option()

        option.excerciseStartDate = exercise
        option.tradeDate = trade
        option.expiryDate = expiry

        option
    }
}
