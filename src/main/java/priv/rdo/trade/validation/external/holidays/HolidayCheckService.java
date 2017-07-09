package priv.rdo.trade.validation.external.holidays;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class HolidayCheckService {
    private static final XLogger LOG = XLoggerFactory.getXLogger(HolidayCheckService.class);

    private static final String URL = "https://holidayapi.com/v1/holidays?key={key}&country={country}&year={year}&month={month}&day={day}";
    private static final String DEFAULT_COUNTRY = "US";
    // default = US, because our request does NOT contain information about location. one could argue that we could use currency for that, but this method
    // would fail for Europe (EURO). Maybe it could be obtained from the customer or the legal entity, but I don't have such information
    // then again... I suppose this was just to check if I am able to make some rest request so... I did a very simple one :)

    @Value("${holidays.key}")
    private String key;

    private final RestTemplate restTemplate;

    public HolidayCheckService(RestTemplate restTemplate, HolidayCheckServiceErrorHandler errorHandler) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(errorHandler);
    }

    @Timed
    public boolean isHoliday(LocalDate date) {
        LOG.info("Calling external system to check if {} is a holiday", date);

        ResponseEntity<HolidayApiResponse> response;

        response = restTemplate.getForEntity(URL, HolidayApiResponse.class,
                key,
                DEFAULT_COUNTRY,
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth()
        );

        return LOG.exit(containsHolidays(response.getBody()));
    }

    boolean containsHolidays(HolidayApiResponse response) {
        return CollectionUtils.isNotEmpty(response.getHolidays());
    }
}
