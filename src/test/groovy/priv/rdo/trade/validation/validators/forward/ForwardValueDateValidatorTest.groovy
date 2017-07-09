package priv.rdo.trade.validation.validators.forward

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
class ForwardValueDateValidatorTest extends Specification {
    ForwardValueDateValidator sut = new ForwardValueDateValidator()

    @Unroll
    def "should #not try to validate #input"() {
        expect:
            sut.shouldValidate(input) == expectedResult

        where:
            input         || expectedResult
            new Spot()    || false
            new Option()  || false
            new Trade()   || false
            new Forward() || true
            null          || false

            not = expectedResult ? "" : "not"
    }

    @Unroll
    def "should not validate '#input'"() {
        when:
            ValidationResult result = sut.validate(testForward(input))

        then:
            !result.success
            result.message.contains(expectedMessage)

        where:
            input                     || expectedMessage
            LocalDate.of(2017, 9, 16) || "the value date should be a 3rd Friday of a quarter"
            LocalDate.of(2016, 9, 17) || "the value date should be a 3rd Friday of a quarter"
            null                      || "fields are mandatory"
    }

    @Unroll
    def "should validate '#input'"() {
        when:
            ValidationResult result = sut.validate(testForward(input))

        then:
            result.success
            result.message == null

        where:
            input                     || _
            LocalDate.of(2017, 9, 15) || _
            LocalDate.of(2017, 6, 16) || _
    }

    @Unroll
    def "should return #expectedResult for #valueDate"() {
        expect:
            sut.isA3rdFridayOfAQuarter(valueDate) == expectedResult

        where:
            valueDate                 || expectedResult
            LocalDate.of(2017, 9, 15) || true
            LocalDate.of(2017, 9, 14) || false
            LocalDate.of(2017, 9, 1)  || false
            LocalDate.of(2017, 9, 8)  || false
            LocalDate.of(2017, 9, 22) || false
            LocalDate.of(2017, 9, 29) || false
            LocalDate.of(2017, 9, 29) || false
            LocalDate.of(2017, 2, 15) || false
    }

    Forward testForward(LocalDate valueDate) {
        Forward forward = new Forward()

        forward.valueDate = valueDate

        forward
    }
}
