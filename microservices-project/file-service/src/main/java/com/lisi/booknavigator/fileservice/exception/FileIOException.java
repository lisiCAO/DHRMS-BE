package com.lisi.booknavigator.fileservice.exception;

import java.io.IOException;

/**
 * Custom exception for handling I/O errors specific to file operations in the file service.
 */
public class FileIOException extends IOException {

    /**
     * Constructs a new FileIOException with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     */
    public FileIOException(String message) {
        super(message);
    }

    /**
     * Constructs a new FileIOException with the specified detail message and cause.
     *
     * Note that the detail message associated with cause is not automatically incorporated into
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method).
     */
    public FileIOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new FileIOException with the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method).
     */
    public FileIOException(Throwable cause) {
        super(cause);
    }
}
