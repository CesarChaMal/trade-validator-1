package priv.rdo.trade.validation.external.holidays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class Holiday {
    private String name;
    private LocalDate date;
}
