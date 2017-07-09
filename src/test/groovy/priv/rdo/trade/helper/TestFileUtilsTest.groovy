package priv.rdo.trade.helper

import spock.lang.Specification

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class TestFileUtilsTest extends Specification {
    def "should read spot_ok.json file"() {
        when:
            String toString = TestFileUtils.fileToString("spot_ok.json")

        then:
            toString == "{  \"customer\": \"PLUTO1\",  \"ccyPair\": \"EURUSD\",  \"type\": \"Spot\",  \"direction\": \"BUY\",  \"tradeDate\": \"2016-08-14\",  \"amount1\": 1000000.00,  \"amount2\": 1120000.00,  \"rate\": 1.12,  \"valueDate\": \"2016-08-16\",  \"legalEntity\": \"CS Zurich\",  \"trader\": \"Johann Baumfiddler\"}"
    }
}
