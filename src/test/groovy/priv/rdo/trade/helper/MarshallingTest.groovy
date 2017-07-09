package priv.rdo.trade.helper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import priv.rdo.trade.model.input.*
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

/**
 * @author WrRaThY
 * @since 08.07.2017
 */
class MarshallingTest extends Specification {
    public static final String TEST_JSON = '{"customer":"PLUTO1","ccyPair":"EURUSD","type":"Spot","direction":"BUY","tradeDate":"2016-08-11",' +
            '"amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-15","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"}'

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())

    def "should unmarshal JSON to an object"() {

        when:
            def result = mapper.readValue(TEST_JSON, Trade)

        then:
            result != null
            result.ccyPair.leftCurrency == Currency.getInstance(Locale.GERMANY)
            result.direction == TradeDirection.BUY
    }

    def "should marshal an object to JSON"() {
        given:
            Spot input = testSpot()


        when:
            String result = mapper.writeValueAsString(input)

        then:
            result == TEST_JSON
    }

    def testSpot() {
        Spot spot = new Spot()
        spot.customer = Customer.PLUTO1
        spot.ccyPair = new CurrencyPair("EURUSD")
        spot.type = "Spot"
        spot.direction = TradeDirection.BUY
        spot.tradeDate = LocalDate.of(2016, Month.AUGUST, 11)
        spot.amount1 = BigDecimal.valueOf(1000000)
        spot.amount2 = BigDecimal.valueOf(1120000)
        spot.rate = BigDecimal.valueOf(1.12)
        spot.valueDate = LocalDate.of(2016, Month.AUGUST, 15)
        spot.legalEntity = "CS Zurich"
        spot.trader = "Johann Baumfiddler"

        spot
    }
}
