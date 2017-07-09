package priv.rdo.trade.model.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import priv.rdo.trade.config.MarshallingConfig.MoneySerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = Trade.class) //thanks to this json parser will not throw an exception and we will be able to add type checking to the validation chain
@JsonSubTypes({
        @Type(value = Spot.class, name = TradeType.SPOT),
        @Type(value = Forward.class, name = TradeType.FORWARD),
        @Type(value = Option.class, name = TradeType.VANILLA_OPTION)
})
public class Trade {
    @JsonIgnore //for internal use only
    private UUID requestId;

    @ApiModelProperty(required = true, example = "PLUTO1")
    private String customer;

    @ApiModelProperty(required = true, example = "EURUSD")
    private CurrencyPair ccyPair;

    @ApiModelProperty(required = true, example = "Spot")
    private String type; //refer to TradeType for more info

    @ApiModelProperty(required = true, example = "BUY")
    private TradeDirection direction;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(required = true, example = "2016-01-01")
    private LocalDate tradeDate;

    @ApiModelProperty(required = true, example = "100.00")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal amount1;

    @ApiModelProperty(required = true, example = "110.00")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal amount2;

    @ApiModelProperty(required = true, example = "1.12")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal rate;

    @ApiModelProperty(required = true, example = "2016-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate valueDate;

    @ApiModelProperty(required = true, example = "CS Zurich")
    private String legalEntity;

    @ApiModelProperty(required = true, example = "Mr Anana Kofana")
    private String trader;


    Trade() {
        requestId = UUID.randomUUID(); //good enough for now
    }


    /*
     * VERY long comment
     * I wanted to discuss one of the requirements. Given the opportunity I'd ask about it, but I cannot...
     * Requirement: The validation response should include information about errors detected in the trade (in case multiple are detected, all of them should be returned)
     * My opinion: I think it makes sense to return such a list on case of business validations (for example: value date cannot fall on weekend or non-working day),
     * but for syntax errors (for example rate is not a number, trade date is not a date, etc) adding @Valid annotation on our endpoint and using built-in mechanisms to
     * detect such problems is a much better idea.
     * pros:
     *  Code is shorter (one annotation per field instead of a new validator and some generic error handler, which we need anyway)
     *  Code is well tested (those annotations use library methods used all over the world. And we can always forget about some null-check...)
     *  Less requests will actually hit our logic, which means servers might be less busy with possibly heavy processing (here - WorkingDateValidator calls external service)
     *
     * That said... I'll transform everything to String and validate inside the code to actually live up to this requirement
     * edit: Aaaaand I transformed it back to how it was. Java is a Strongly typed language, not 'String'ly typed... lets use objects!
     *
     * another note:
     * this class is polluted with validation and swagger annotations. normally I'd separate view model from domain model, but given the site of the project and timeline...
     */
}
