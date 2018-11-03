package binar.box.rest;

import org.springframework.web.bind.annotation.ControllerAdvice;

import binar.box.dto.LockBridgesExceptionDTO;
import binar.box.util.LockBridgesException;

@ControllerAdvice
public class ExceptionHandler {

	private LockBridgesExceptionDTO lockBridgesException(LockBridgesException lockBridges) {
		return new LockBridgesExceptionDTO(lockBridges);
	}
}
