package priv.rdo.trade.validation.validators.option

import priv.rdo.trade.model.input.Forward
import priv.rdo.trade.model.input.Option
import priv.rdo.trade.model.input.Spot
import priv.rdo.trade.model.input.Trade
import priv.rdo.trade.model.output.ValidationResult
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class OptionDatesValidatorTest extends Specification {
    OptionDatesValidator sut = new OptionDatesValidator()

    @Unroll
    def "should #not try to validate #input"() {
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
    def "should return #expectedMessage after validation for premium='#premium', delivery='#delivery', expiry='#expiry'"() {
        when:
            ValidationResult result = sut.validate(testOption(premium, delivery, expiry))

        then:
            !result.success
            result.message.contains(expectedMessage)

        where:
            premium | delivery | expiry  || expectedMessage
            null    | date(8)  | date(9) || "fields are mandatory"
            date(7) | null     | date(9) || "fields are mandatory"
            date(7) | date(8)  | null    || "fields are mandatory"
            date(7) | date(7)  | date(9) || "the expiry date and the premium date shall be before the delivery date"
            date(7) | date(6)  | date(9) || "the expiry date and the premium date shall be before the delivery date"
    }

    @Unroll
    def "should positively validate premium='#premium', delivery='#delivery', expiry='#expiry'"() {
        when:
            ValidationResult result = sut.validate(testOption(premium, delivery, expiry))

        then:
            result.success
            result.message == null

        where:
            premium | delivery | expiry
            date(7) | date(9)  | date(8)
            date(7) | date(9)  | date(5)
    }

    def date(int day) {
        LocalDate.of(2017, 7, day)
    }

    Option testOption(LocalDate premium, LocalDate delivery, LocalDate expiry) {
        Option trade = new Option()

        trade.premiumDate = premium
        trade.deliveryDate = delivery
        trade.expiryDate = expiry

        trade
    }
}
