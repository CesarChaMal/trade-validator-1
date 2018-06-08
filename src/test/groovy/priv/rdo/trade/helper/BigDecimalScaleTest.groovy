package priv.rdo.trade.helper

import spock.lang.Specification

class BigDecimalScaleTest extends Specification {
    def "properly set scale for BigDecimal"() {
        given:
            BigDecimal input = new BigDecimal(10)

        when:
            BigDecimal output = input.setScale(2, BigDecimal.ROUND_HALF_UP)

        then:
            input.toString() == "10"
            output.toString() == "10.00"
    }
}
