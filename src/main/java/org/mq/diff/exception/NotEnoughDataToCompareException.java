package org.mq.diff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "No enough data to be compared")
public class NotEnoughDataToCompareException extends RuntimeException {

}
