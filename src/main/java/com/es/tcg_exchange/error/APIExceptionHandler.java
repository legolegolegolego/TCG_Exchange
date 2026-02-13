package com.es.tcg_exchange.error;

import com.es.tcg_exchange.error.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import org.springframework.security.core.AuthenticationException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessageForClient handleBadRequest(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({NotFoundException.class, NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessageForClient handleNotFound(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({UnauthorizedException.class, AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorMessageForClient handleUnauthorized(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({InternalServerErrorException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessageForClient handleInternalServerError(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({ForbiddenException.class, HttpClientErrorException.Forbidden.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessageForClient handleForbidden(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({DuplicateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorMessageForClient handleDuplicate(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

}
