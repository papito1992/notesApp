package com.mycompany.myapp.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class NoteExpirationDateException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public NoteExpirationDateException() {
        super(ErrorConstants.NOTE_EXPIRED, "Note content has reached its expiration date.", Status.UNAUTHORIZED);
    }
}
