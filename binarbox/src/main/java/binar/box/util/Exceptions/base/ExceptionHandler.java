package binar.box.util.Exceptions.base;

import binar.box.dto.LockBridgesExceptionDTO;
import binar.box.util.Exceptions.EntityNotFoundException;
import binar.box.util.Exceptions.FieldsException;
import binar.box.util.Exceptions.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class ExceptionHandler {

	@Autowired
	private ErrorInfoFactory errorInfoFactory;

	@org.springframework.web.bind.annotation.ExceptionHandler(value = {FieldsException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorInfo handleFieldsException(FieldsException e) {
		return ErrorInfoFactory.buildBadRequestErrorInfo("Fields are incorrect", "msg.key", e.getResult());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = {ConstraintViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorInfo handleExec(ConstraintViolationException e) {
		List<String> message = e.getConstraintViolations()
				.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());
		return ErrorInfo.builder().status(HttpStatus.BAD_REQUEST.value()).messageKey("constraint.message")
				.message(message.toString()).developerMessage(e.getMessage()).build();
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = {EntityNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorInfo handleConflictException(EntityNotFoundException e) {
		return ErrorInfo.builder().status(HttpStatus.NOT_FOUND.value()).messageKey("not.found.message")
				.message("Not Found Message").developerMessage(e.getMessage()).build();
	}

	private LockBridgesExceptionDTO lockBridgesException(LockBridgesException lockBridges) {
		return new LockBridgesExceptionDTO(lockBridges);
	}
}
