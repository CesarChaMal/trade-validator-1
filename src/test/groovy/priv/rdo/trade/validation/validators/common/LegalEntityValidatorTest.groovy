package priv.rdo.trade.validation.validators.common

import priv.rdo.trade.model.input.Forward
import priv.rdo.trade.model.input.Option
import priv.rdo.trade.model.input.Spot
import priv.rdo.trade.model.input.Trade
import priv.rdo.trade.model.output.ValidationResult
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class LegalEntityValidatorTest extends Specification {
    LegalEntityValidator sut = new LegalEntityValidator()

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
            "asdasd" || "We do not support an named"
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
            input       || _
            "CS Zurich" || _
    }

    Trade testTrade(String legalEntity) {
        Trade trade = new Trade()

        trade.legalEntity = legalEntity

        trade
    }
}
