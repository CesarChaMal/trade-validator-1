package priv.rdo.trade.endpoint;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import priv.rdo.trade.model.output.BulkTradeValidationResult;
import priv.rdo.trade.model.input.Trade;
import priv.rdo.trade.model.output.TradeValidationResult;
import priv.rdo.trade.model.output.ValidationResult;
import priv.rdo.trade.validation.TradeValidationService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TradeValidationEndpoint {
    private static final XLogger LOG = XLoggerFactory.getXLogger(TradeValidationEndpoint.class);

    private final TradeValidationService tradeValidationService;

    public TradeValidationEndpoint(TradeValidationService tradeValidationService) {
        this.tradeValidationService = tradeValidationService;
    }

    @Timed
    @PostMapping(value = "trades")
    public ResponseEntity<TradeValidationResult> trades(@RequestBody @Valid Trade trade) {
        LOG.info("Received a trade to validate {}", trade);

        List<ValidationResult> validationErrors = tradeValidationService.validate(trade);

        if (validationErrors.isEmpty()) {
            LOG.info("Trade validation success {}", trade);
            return ResponseEntity.ok(new TradeValidationResult(trade));
        } else {
            LOG.info("Trade validation failed {} {}", trade, validationErrors);
            return ResponseEntity.badRequest().body(new TradeValidationResult(trade, validationErrors));
        }
    }

    @Timed
    @PostMapping(value = "bulkTrades")
    public ResponseEntity<BulkTradeValidationResult> trades(@RequestBody @Valid List<Trade> trades) {
        LOG.info("Received trades to validate {}", trades);

        BulkTradeValidationResult result = new BulkTradeValidationResult();

        trades.forEach(trade -> result.add(partialResultForBulk(trade)));

        if (result.containsErrors()) {
            LOG.info("Trade validation failure {}", result);
            return LOG.exit(ResponseEntity.badRequest().body(result));
        } else {
            LOG.info("Trade validation success {}", result);
            return LOG.exit(ResponseEntity.ok(result));
        }
    }

    private TradeValidationResult partialResultForBulk(Trade trade) {
        List<ValidationResult> result = tradeValidationService.validate(trade);
        if (CollectionUtils.isEmpty(result)){
            return new TradeValidationResult(trade);
        }
        return new TradeValidationResult(trade, result);
    }
}
