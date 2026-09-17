package com.manager.files.exception;

public class FileAlreadyExistException extends RuntimeException {
    public FileAlreadyExistException(String message) {
        super(message);
    }
}