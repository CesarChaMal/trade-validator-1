package priv.rdo.trade.model.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Getter
@Setter
@ToString(callSuper = true)
public class Option extends Trade {
    private String style;
    private String strategy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(required = true, example = "2016-01-01")
    private LocalDate deliveryDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(required = true, example = "2016-01-01")
    private LocalDate expiryDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(required = true, example = "2016-01-01")
    private LocalDate excerciseStartDate;
    private Currency payCcy;
    private BigDecimal premium;
    private Currency premiumCcy;
    private String premiumType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(required = true, example = "2016-01-01")
    private LocalDate premiumDate;
}
