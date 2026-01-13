package ru.batorfly.consumer;

public class MessageConsumingException extends RuntimeException {
    public MessageConsumingException(){}

    public MessageConsumingException(final String description) {
        super(description);
    }
    public MessageConsumingException(final String description, final Throwable cause) {
        super(description, cause);
    }
    public MessageConsumingException(final Throwable cause) {
        super(cause);
    }
}
