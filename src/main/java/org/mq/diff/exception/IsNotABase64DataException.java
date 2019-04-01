package org.mq.diff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Data is not a base64 encoded")
public class IsNotABase64DataException extends RuntimeException {

}
