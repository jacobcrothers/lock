package binar.box.rest;

import binar.box.dto.LockBridgesExceptionDTO;
import binar.box.util.LockBridgesException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@ControllerAdvice
public class ExceptionHandler {

    private LockBridgesExceptionDTO lockBridgesException(LockBridgesException lockBridges) {
        return new LockBridgesExceptionDTO(lockBridges);
    }
}
