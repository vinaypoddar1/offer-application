package com.heavenhr.humanresourceservice.advices;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.heavenhr.humanresourceservice.errors.RestError;
import com.heavenhr.humanresourceservice.exceptions.InvalidApplicantInfoException;
import com.heavenhr.humanresourceservice.exceptions.InvalidOfferException;
import com.heavenhr.humanresourceservice.exceptions.NoContentException;

@EnableWebMvc
@ControllerAdvice
public class GenericControllerAdvice {

	@ExceptionHandler
	public ResponseEntity<?> handleException(MethodArgumentNotValidException exception) {

		String errorMsg = exception.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst().orElse(exception.getMessage());

		return new ResponseEntity<>(new RestError(HttpStatus.BAD_REQUEST, errorMsg), HttpStatus.BAD_REQUEST);
	}

	@ResponseBody
	@ExceptionHandler({ NoContentException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public RestError handleException(NoContentException exception) {
		return new RestError(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ResponseBody
	@ExceptionHandler({ InvalidOfferException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestError handleException(InvalidOfferException exception) {
		return new RestError(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ResponseBody
	@ExceptionHandler({ InvalidApplicantInfoException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestError handleException(InvalidApplicantInfoException exception) {
		return new RestError(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

}
