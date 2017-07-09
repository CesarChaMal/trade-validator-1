package priv.rdo.trade.model

import priv.rdo.trade.model.input.CurrencyPair
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author WrRaThY
 * @since 08.07.2017
 */
class CurrencyPairTest extends Specification {
    def static DOLLAR = Currency.getInstance(Locale.US)
    def static EURO = Currency.getInstance(Locale.GERMANY)
    def static PLN = Currency.getInstance("PLN")

    @Unroll
    def "should parse input(#input) to objects(left = #expectedLeftCurrency, right = #expectedRightCurrency)"() {
        when:
            CurrencyPair result = new CurrencyPair(input)

        then:
            result.getLeftCurrency() == expectedLeftCurrency
            result.getRightCurrency() == expectedRightCurrency

        where:
            input      || expectedLeftCurrency | expectedRightCurrency
            "EURUSD"   || EURO                 | DOLLAR
            "PLNUSD"   || PLN                  | DOLLAR
            "PLDUSD"   || null                 | DOLLAR
            "PLN12USD" || PLN                  | null
            "PLNUSD1"  || PLN                  | null
            "PLSUSZ"   || null                 | null
            ""         || null                 | null
            null       || null                 | null
    }
}
