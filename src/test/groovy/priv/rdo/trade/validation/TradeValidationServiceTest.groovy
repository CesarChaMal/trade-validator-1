package priv.rdo.trade.validation

import priv.rdo.trade.model.output.ValidationResult
import spock.lang.Specification
import spock.lang.Unroll

import static priv.rdo.trade.model.output.ValidationResult.SUCCESS
import static priv.rdo.trade.model.output.ValidationResult.failure

/**
 * @author WrRaThY
 * @since 08.07.2017
 */
class TradeValidationServiceTest extends Specification {
    TradeValidationService sut = new TradeValidationService(Mock(ValidationManager))

    @Unroll
    def "should return #expectedFailuresCount fails for #validationResults"() {
        when: ""
            List<ValidationResult> errors = sut.findValidationErrors(validationResults.stream())

        then:
            errors.size() == expectedFailuresCount

        where:
            validationResults                              || expectedFailuresCount
            [SUCCESS, failure("aaa", "bbb")]               || 1
            [failure("aaa", "bbb")]                        || 1
            [SUCCESS, SUCCESS, SUCCESS]                    || 0
            [failure("aaa", "bbb"), failure("aaa", "bbb")] || 2
            []                                             || 0
    }
}
