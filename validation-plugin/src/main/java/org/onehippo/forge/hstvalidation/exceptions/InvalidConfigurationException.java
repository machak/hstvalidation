package org.onehippo.forge.hstvalidation.exceptions;

/**
 * @version $Id: InvalidConfigurationException.java 33 2011-07-26 23:15:14Z mmilicevic $
 */
public class InvalidConfigurationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidConfigurationException() {

    }

    public InvalidConfigurationException(final String message) {
        super(message);
    }

    public InvalidConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigurationException(final Throwable cause) {
        super(cause);
    }
}
