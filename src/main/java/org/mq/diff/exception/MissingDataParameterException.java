package org.mq.diff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Data parameter is missing")
public class MissingDataParameterException extends RuntimeException {

}
