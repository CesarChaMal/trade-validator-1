package priv.rdo.trade.validation.validators.common

import priv.rdo.trade.model.input.Forward
import priv.rdo.trade.model.input.Option
import priv.rdo.trade.model.input.Spot
import priv.rdo.trade.model.input.Trade
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
/**
 * @author WrRaThY
 * @since 08.07.2017
 */
class CustomerNameValidatorTest extends Specification {
    @Shared
    CustomerNameValidator sut = new CustomerNameValidator()

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
    def "should #not find match for #input customer name"() {
        expect:
            sut.isSupportedCustomer(input) == expected

        where:
            input    || expected
            "PLUTO2" || true
            "PLUTO3" || false
            "asdasd" || false
            ""       || false
            null     || false

            not = expected ? "" : "not"
    }

    @Unroll
    def "should return #resType validation result for #input customer name"() {
        given:
            Trade trade = testTrade(input)

        expect:
            sut.validate(trade).success == expectedSuccess

        where:
            input    || expectedSuccess | fieldName
            "PLUTO2" || true            | "customer"
            "PLUTO3" || false           | "customer"
            "asdasd" || false           | "customer"
            ""       || false           | "customer"
            null     || false           | "customer"

            resType = expectedSuccess ? "positive" : "negative"
    }

    Trade testTrade(String customerName) {
        Trade trade = new Spot()

        trade.customer = customerName

        trade
    }
}
