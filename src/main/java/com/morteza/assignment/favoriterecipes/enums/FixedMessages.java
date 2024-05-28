package com.morteza.assignment.favoriterecipes.enums;

public enum FixedMessages {
    NOT_FOUND_RESOURCE("Not found %s!"),
    NOT_FOUND_RESOURCE_WITH_ID("Not found %s with id %d!");

    private final String message;

    FixedMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
