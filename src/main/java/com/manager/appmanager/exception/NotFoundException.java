package com.manager.appmanager.exception;

public class NotFoundException extends Exception {
    public NotFoundException(int id) {
        super("Couldn't find object with id " + id);
    }
}
