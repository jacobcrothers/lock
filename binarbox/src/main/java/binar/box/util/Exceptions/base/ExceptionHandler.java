package binar.box.util.Exceptions.base;

import binar.box.util.Exceptions.LockBridgesException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import binar.box.dto.LockBridgesExceptionDTO;

@ControllerAdvice
public class ExceptionHandler {

	private LockBridgesExceptionDTO lockBridgesException(LockBridgesException lockBridges) {
		return new LockBridgesExceptionDTO(lockBridges);
	}
}
