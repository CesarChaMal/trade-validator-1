package priv.rdo.trade.validation.external.holidays;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

import static org.slf4j.ext.XLogger.Level.WARN;

@Component
class HolidayCheckServiceErrorHandler extends DefaultResponseErrorHandler {
    private static final XLogger LOG = XLoggerFactory.getXLogger(HolidayCheckServiceErrorHandler.class);

    private final ObjectMapper mapper;

    public HolidayCheckServiceErrorHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        LOG.entry(response);

        if (HttpStatus.NOT_FOUND.equals(getHttpStatusCode(response))) {
            throw LOG.throwing(WARN, new HolidayCheckServiceNotWorkingException("Service was not found, please contact admins"));
        }
        try {
            HolidayApiResponse apiResponse = mapper.readValue(getResponseBody(response), HolidayApiResponse.class);
            throw LOG.throwing(WARN, new HolidayCheckServiceNotWorkingException("Service did not respond correctly. status = "
                    + apiResponse.getStatus() + ", error = " + apiResponse.getError()));
        } catch (IOException e) {
            LOG.warn("Exception not handled internally", e);
            super.handleError(response);
        }
    }

}
