package com.tl.transacciones.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public final class ExceptionTemplate {

    private final Date timestamp;
    private final Integer status;
    private final String error;
    private final List<String> reasons;
    private final String path;

    public ExceptionTemplate(HttpStatus status, ServletWebRequest wr) {
        this.timestamp = new Date(System.currentTimeMillis());
        this.status = status.value();
        this.error = StringUtils.capitalize(status.getReasonPhrase());
        this.reasons = new ArrayList<>();
        this.path = wr.getRequest().getRequestURI();
    }

    public ExceptionTemplate(HttpStatus status, ServletWebRequest wr, String reason) {
        this(status, wr);
        this.reasons.add(reason);
    }

    public ExceptionTemplate(HttpStatus status, ServletWebRequest wr, List<String> reasons) {
        this(status, wr);
        this.reasons.addAll(reasons);
    }
}
