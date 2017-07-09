package priv.rdo.trade.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString
public class ValidationResult {
    public static final ValidationResult SUCCESS = buildSuccess();

    @JsonIgnore
    private boolean success;
    private String fieldName;
    private String message;

    private ValidationResult() {

    }

    private static ValidationResult buildSuccess() {
        ValidationResult result = new ValidationResult();

        result.success = true;

        return result;
    }

    public static ValidationResult success() {
        return SUCCESS;
    }

    public static ValidationResult failureFieldsMandatory(String... fieldNames) {
        return failure("the following fields are mandatory", fieldNames);
    }

    public static ValidationResult failure(String message, String... fieldNames) {
        ValidationResult result = new ValidationResult();

        result.success = false;
        result.fieldName = Arrays.toString(fieldNames);
        result.message = message;

        return result;
    }

    @JsonIgnore
    public boolean isFailure() {
        return !success;
    }
}
