package binar.box.dto;

import binar.box.util.LockBridgesException;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
public class LockBridgesExceptionDTO {
    private String message;
    private String cause;

    public LockBridgesExceptionDTO() {
    }

    public LockBridgesExceptionDTO(LockBridgesException lockBridges) {
        this.message = lockBridges.getMessage();
        this.cause = lockBridges.getCause().getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
