package priv.rdo.trade.validation.external.holidays;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
class HolidayApiResponse {
    private int status;
    private List<Holiday> holidays;
    private String error;
}
