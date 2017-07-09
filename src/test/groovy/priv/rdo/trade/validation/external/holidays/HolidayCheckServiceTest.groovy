package priv.rdo.trade.validation.external.holidays

import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static java.time.LocalDate.now

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class HolidayCheckServiceTest extends Specification {
    HolidayCheckService sut = new HolidayCheckService(Mock(RestTemplate), Mock(HolidayCheckServiceErrorHandler))

    @Unroll
    def "should #not contain holidays #expectedResult for #input"() {
        expect: ""
            sut.containsHolidays(input) == expectedResult

        where:
            input                                                                                 || expectedResult
            testResponse([new Holiday("testHoliday", now())])                                     || true
            testResponse([new Holiday("testHoliday", now()), new Holiday("testHoliday2", now())]) || true
            testResponse([])                                                                      || false
            testResponse(null)                                                                    || false

            not = expectedResult ? "" : "not"
    }

    HolidayApiResponse testResponse(List<Holiday> holidays) {
        HolidayApiResponse response = new HolidayApiResponse()

        response.holidays = holidays

        response
    }
}
