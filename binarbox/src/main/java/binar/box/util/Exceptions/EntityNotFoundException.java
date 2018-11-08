/**
 *
 */
package binar.box.util.Exceptions;

import binar.box.util.Exceptions.base.BaseLocalizedException;

public class EntityNotFoundException extends BaseLocalizedException {
	private static final long serialVersionUID = -6237901329312044859L;

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(String message, String messageKey, Object... messageArguments) {
		super(message, messageKey, messageArguments);
	}
}
