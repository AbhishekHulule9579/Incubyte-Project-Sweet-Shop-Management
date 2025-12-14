package com.sweetcorner.incubyte_backend.dto;

/**
 * Data Transfer Object for returning error messages to the client.
 */
public class ErrorResponse {
    /**
     * The error message to be displayed.
     */
    private String message;

    /**
     * Constructor to initialize ErrorResponse with a message.
     *
     * @param message The error message.
     */
    public ErrorResponse(String message) {
        this.message = message;
    }

    /**
     * Retrieves the error message.
     *
     * @return The error message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     *
     * @param message The error message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
