package com.mycompany.myapp.service;

public class NoteExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoteExpiredException() {
        super("Note has expired.");
    }
}
