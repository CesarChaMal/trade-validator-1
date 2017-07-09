package priv.rdo.trade.validation.validators.common

import priv.rdo.trade.model.input.Forward
import priv.rdo.trade.model.input.Option
import priv.rdo.trade.model.input.Spot
import priv.rdo.trade.model.input.Trade
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

/**
 * @author WrRaThY
 * @since 08.07.2017
 */
class BasicTradeAndValueDatesValidatorTest extends Specification {
    @Shared
    BasicTradeAndValueDatesValidator sut = new BasicTradeAndValueDatesValidator()

    @Unroll
    def "should #not try to validate #input"() {
        expect:
            sut.shouldValidate(input) == expectedResult

        where:
            input         || expectedResult
            new Spot()    || true
            new Option()  || false
            new Trade()   || false
            new Forward() || true
            null          || false

            not = expectedResult ? "" : "not"
    }

    @Unroll
    def "should return #resType validation result for '#tradeDate' tradeDate and '#valueDate' valueDate"() {
        given:
            Trade trade = testTrade(tradeDate, valueDate)

        expect:
            sut.validate(trade).success == expectedSuccess

        where:
            tradeDate                  | valueDate                  || expectedSuccess
            LocalDate.of(2017, 05, 13) | LocalDate.of(2017, 05, 13) || true
            LocalDate.of(2017, 05, 13) | LocalDate.of(2017, 05, 14) || true
            LocalDate.of(2017, 05, 13) | LocalDate.of(2018, 05, 14) || true
            LocalDate.of(2017, 05, 13) | LocalDate.of(2017, 05, 12) || false
            LocalDate.of(2017, 05, 13) | LocalDate.of(2016, 05, 13) || false
            LocalDate.of(2017, 05, 13) | null                       || false
            null                       | LocalDate.of(2016, 05, 13) || false
            null                       | null                       || false

            resType = expectedSuccess ? "positive" : "negative"
    }

    Trade testTrade(LocalDate tradeDate, LocalDate valueDate) {
        Trade trade = new Spot()

        trade.tradeDate = tradeDate
        trade.valueDate = valueDate

        trade
    }
}
