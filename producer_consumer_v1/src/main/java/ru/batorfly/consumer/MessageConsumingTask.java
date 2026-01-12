package ru.batorfly.consumer;

import ru.batorfly.broker.MessageBroker;
import ru.batorfly.model.Message;

import java.util.concurrent.TimeUnit;

public class MessageConsumingTask  implements Runnable {
    private static final String EOL = System.lineSeparator();
    private static final String MESSAGE_CONSUMED_TEMPLATE = "Message '%s' is consumed" + EOL;

    private static final int SECOND_DURATION_BEFORE_CONSUME = 1;

    private final MessageBroker messageBroker;

    public MessageConsumingTask(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(SECOND_DURATION_BEFORE_CONSUME);
                final Message consumedMessage = messageBroker.consume();
                System.out.printf(MESSAGE_CONSUMED_TEMPLATE, consumedMessage);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
