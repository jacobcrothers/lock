package binar.box.util.Exceptions;

import binar.box.util.Exceptions.base.BaseLocalizedException;

public class LockBridgesException extends BaseLocalizedException {

	public LockBridgesException(String lockBridgesMessage) {
		super(lockBridgesMessage);
	}

	public LockBridgesException(String message, String messageKey, Object... messageArguments) {
		super(message, messageKey, messageArguments);
	}

}
