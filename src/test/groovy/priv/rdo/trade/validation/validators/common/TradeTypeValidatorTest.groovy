package priv.rdo.trade.validation.validators.common

import priv.rdo.trade.model.input.*
import priv.rdo.trade.model.output.ValidationResult
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class TradeTypeValidatorTest extends Specification {
    TradeTypeValidator sut = new TradeTypeValidator()

    @Unroll
    def "should #not try to validate #input"() {
        expect:
            sut.shouldValidate(input) == expectedResult

        where:
            input         || expectedResult
            new Spot()    || true
            new Option()  || true
            new Trade()   || true
            new Forward() || true
            null          || true

            not = expectedResult ? "" : "not"
    }

    @Unroll
    def "should not validate '#input'"() {
        when:
            ValidationResult result = sut.validate(testTrade(input))

        then:
            !result.success
            result.message.contains(expectedMessage)

        where:
            input    || expectedMessage
            "asdasd" || "Type has to be one of"
            ""       || "fields are mandatory"
            null     || "fields are mandatory"
    }

    @Unroll
    def "should validate '#input'"() {
        when:
            ValidationResult result = sut.validate(testTrade(input))

        then:
            result.success
            result.message == null

        where:
            input                    || _
            TradeType.FORWARD        || _
            TradeType.SPOT           || _
            TradeType.VANILLA_OPTION || _
    }

    Trade testTrade(String type) {
        Trade trade = new Trade()

        trade.type = type

        trade
    }
}
