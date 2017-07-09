package priv.rdo.trade.validation.validators.option

import priv.rdo.trade.model.input.Forward
import priv.rdo.trade.model.input.Option
import priv.rdo.trade.model.input.Spot
import priv.rdo.trade.model.input.Trade
import priv.rdo.trade.model.output.ValidationResult
import spock.lang.Specification
import spock.lang.Unroll

import static priv.rdo.trade.model.input.OptionStyle.AMERICAN
import static priv.rdo.trade.model.input.OptionStyle.EUROPEAN

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class OptionStyleValidatorTest extends Specification {
    OptionStyleValidator sut = new OptionStyleValidator()

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
    def "should not validate '#input'"() {
        when:
            ValidationResult result = sut.validate(testOption(input))

        then:
            result.success == expectedSuccess
            result.message.contains(expectedMessage)

        where:
            input           || expectedSuccess | expectedMessage
            "asd"           || false           | "This field should be one of"
            ""              || false           | "This field should be one of"
            null            || false           | "fields are mandatory"
    }

    @Unroll
    def "should validate '#input'"() {
        when:
            ValidationResult result = sut.validate(testOption(input))

        then:
            result.success == expectedSuccess
            result.message == null

        where:
            input           || expectedSuccess
            AMERICAN.name() || true
            EUROPEAN.name() || true
    }

    @Unroll
    def "style is #not valid for #input"() {
        expect:
            sut.validStyle(input) == expectedResult

        where:
            input          || expectedResult
            AMERICAN.name() | true
            EUROPEAN.name() | true
            "asd"          || false
            ""             || false
            null           || false

            not = expectedResult ? "" : "not"
    }

    Option testOption(String style) {
        Option trade = new Option()

        trade.style = style

        trade
    }
}
