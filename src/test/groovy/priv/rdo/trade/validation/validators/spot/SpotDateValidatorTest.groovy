package priv.rdo.trade.validation.validators.spot

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
 * @since 08.07.2017
 */
class SpotDateValidatorTest extends Specification {
    SpotDateValidator sut = new SpotDateValidator()

    @Unroll
    def "should #not return success for tradeDate = #tradeDate and valueDate = #valueDate"() {
        given:
            Spot spot = testSpot(tradeDate, valueDate)

        when:
            ValidationResult result = sut.validate(spot)

        then:
            result.isSuccess() == expectedSuccess

        where:
            tradeDate                  | valueDate                 || expectedSuccess
            LocalDate.of(2017, 07, 11) | LocalDate.of(2017, 07, 13) | true
            LocalDate.of(2017, 07, 11) | LocalDate.of(2017, 07, 11) | false
            LocalDate.of(2017, 07, 11) | LocalDate.of(2017, 07, 9)  | false
            null                       | LocalDate.of(2017, 07, 9)  | false
            LocalDate.of(2017, 07, 11) | null                       | false

            not = expectedSuccess ? "" : "not"
    }

    @Unroll
    def "should #not validate #input"() {
        expect:
            sut.shouldValidate(input) == expectedResult

        where:
            input         || expectedResult
            new Spot()    || true
            new Option()  || false
            new Trade()   || false
            new Forward() || false
            null          || false

            not = expectedResult ? "" : "not"
    }

    Spot testSpot(LocalDate tradeDate, LocalDate valueDate) {
        Spot spot = new Spot()

        spot.setTradeDate(tradeDate)
        spot.setValueDate(valueDate)

        spot
    }
}
