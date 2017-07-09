package priv.rdo.trade.validation.external.holidays

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.mock.http.client.MockClientHttpResponse
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
class HolidayCheckServiceErrorHandlerTest extends Specification {
    ObjectMapper mapper = new ObjectMapper()
    HolidayCheckServiceErrorHandler sut = new HolidayCheckServiceErrorHandler(mapper)

    @Unroll
    def "should handle '#expectedMessagePart' situation"() {
        when:
            sut.handleError(new MockClientHttpResponse(mapper.writeValueAsBytes(body), httpCode))

        then:
            def caught = thrown(expectedException)
            caught.getMessage().contains(expectedMessagePart)

        where:
            body                                 | httpCode                           || expectedException                      | expectedMessagePart
            testResponse("not important")        | HttpStatus.NOT_FOUND               || HolidayCheckServiceNotWorkingException | "Service was not found"
            testResponse("Rate limit exceeded.") | HttpStatus.TOO_MANY_REQUESTS       || HolidayCheckServiceNotWorkingException | "Service did not respond correctly."
            "asdasd"                             | HttpStatus.PAYMENT_REQUIRED        || HttpClientErrorException               | "Payment Required"
            "asdasd"                             | HttpStatus.VARIANT_ALSO_NEGOTIATES || HttpServerErrorException               | "Variant Also Negotiates"

    }

    HolidayApiResponse testResponse(String error) {
        HolidayApiResponse response = new HolidayApiResponse()

        response.error = error

        response
    }
}
