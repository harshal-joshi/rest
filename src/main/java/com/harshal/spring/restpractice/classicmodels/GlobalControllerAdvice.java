package com.harshal.spring.restpractice.classicmodels;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.harshal.spring.restpractice.classicmodels.exceptions.CustomerNotFoundException;

@ControllerAdvice
public class GlobalControllerAdvice {

	private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error");
	
	@ExceptionHandler(CustomerNotFoundException.class)
	ResponseEntity<VndErrors> customerNotFoundException(CustomerNotFoundException exception) {
		return error(exception, HttpStatus.NOT_FOUND, exception.getCustomerId() + "");
	}
	
	@ExceptionHandler(DataAccessException.class)
	ResponseEntity<VndErrors> dataAccessException(DataAccessException exception) {
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(this.vndErrorMediaType);
		return new ResponseEntity<>(new VndErrors("DB_ERROR", exception.getRootCause().getMessage()), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private <E extends Exception> ResponseEntity<VndErrors> error(E e, HttpStatus httpStatus, String logref) {
        String msg = Optional.of(e.getMessage()).orElse(e.getClass().getSimpleName());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(this.vndErrorMediaType);
        return new ResponseEntity<>(new VndErrors(logref, msg), httpHeaders, httpStatus);
    }
}
