package com.veegee.polls.business.model.enumeration;

import com.veegee.polls.business.exception.InvalidNextStatusException;

public enum StatusType {
    NEW,
    OPEN,
    CLOSED;

    public StatusType next() {
        switch (this) {
            case NEW:
                return OPEN;
            case OPEN:
            case CLOSED:
                return CLOSED;
            default:
                throw new InvalidNextStatusException();
        }
    }
}
