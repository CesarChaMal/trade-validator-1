package priv.rdo.trade.helper

import org.springframework.web.util.DefaultUriTemplateHandler
import spock.lang.Specification

/**
 * @author WrRaThY
 * @since 08.07.2017
 */
class ExpandUriTest extends Specification {
    private static final String URL = "https://holidayapi.com/v1/holidays?key={key}&country={country}&year={year}&month={month}&day={day}"
    private static final String KEY = "3ed6927f-7746-4eee-8ba5-644c4cc925e4"

    private static final String EXPECTED_URI = "https://holidayapi.com/v1/holidays?key=3ed6927f-7746-4eee-8ba5-644c4cc925e4&country=US&year=2016&month=8&day=15"

    def "should build a nice URI"() {
        when:
            URI expanded = new DefaultUriTemplateHandler().expand(URL, KEY, "US", 2016, 8, 15)

        then:
            expanded.toString() == EXPECTED_URI

    }
}