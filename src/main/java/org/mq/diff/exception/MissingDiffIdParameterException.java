package org.mq.diff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "diffId parameter is missing")
public class MissingDiffIdParameterException extends RuntimeException {

}
