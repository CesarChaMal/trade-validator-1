package priv.rdo.trade.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.StringJoiner;

@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, Object> handle(HttpMessageNotReadableException e) {
        if (e.getRootCause() == null) {
            return ImmutableMap.of("message", "body cannot be empty!");
        }

        if (e.getRootCause() instanceof InvalidFormatException) {
            return ImmutableMap.of("message", buildMessageForInvalidFormat(e));
        }

        return ImmutableMap.of("message", StringUtils.split(e.getRootCause().getMessage(), ":")[0]);
    }

    private String buildMessageForInvalidFormat(HttpMessageNotReadableException e) {
        InvalidFormatException ife = (InvalidFormatException) e.getRootCause();

        StringBuilder msg = new StringBuilder("Invalid value: " + ife.getValue());

        Class<?> targetType = ife.getTargetType();
        if (targetType.isEnum()) {
            StringJoiner joiner = new StringJoiner(", ", ". Possible values: [", "]");

            Enum[] constants = (Enum[]) ife.getTargetType().getEnumConstants();
            for (Enum constant : constants) {
                joiner.add(constant.name());
            }

            msg.append(joiner.toString());
        }
        return msg.toString();
    }
}
