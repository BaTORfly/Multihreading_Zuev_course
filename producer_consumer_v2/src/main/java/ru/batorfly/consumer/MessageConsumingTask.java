package ru.batorfly.consumer;

import ru.batorfly.broker.MessageBroker;
import ru.batorfly.model.Message;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MessageConsumingTask  implements Runnable {
    private static final int SECOND_DURATION_BEFORE_CONSUME = 1;

    private final MessageBroker messageBroker;
    private final int minimalAmountMessagesToConsume;
    private final String name;

    public MessageConsumingTask(MessageBroker messageBroker, int minimalAmountMessagesToConsume, String name) {
        this.messageBroker = messageBroker;
        this.minimalAmountMessagesToConsume = minimalAmountMessagesToConsume;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMinimalAmountMessagesToConsume() {
        return minimalAmountMessagesToConsume;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(SECOND_DURATION_BEFORE_CONSUME);
                final Optional<Message> optionalConsumedMessage = messageBroker.consume(this);
                optionalConsumedMessage.orElseThrow(MessageConsumingException::new);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
