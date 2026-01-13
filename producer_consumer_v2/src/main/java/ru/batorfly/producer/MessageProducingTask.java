package ru.batorfly.producer;

import ru.batorfly.broker.MessageBroker;
import ru.batorfly.model.Message;

import java.util.concurrent.TimeUnit;

public class MessageProducingTask  implements Runnable {
    private static final int SECOND_DURATION_BEFORE_PRODUCE = 1;

    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;
    private final int maximalAmountMessagesToProduce;
    private final String name;

    public MessageProducingTask(MessageBroker messageBroker, MessageFactory messageFactory,
                                int maximalAmountMessagesToProduce, String name) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
        this.maximalAmountMessagesToProduce = maximalAmountMessagesToProduce;
        this.name = name;
    }

    public int getMaximalAmountMessagesToProduce() {
        return maximalAmountMessagesToProduce;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()){
                final Message producedMessage = messageFactory.createAndReturnMessage();
                TimeUnit.SECONDS.sleep(SECOND_DURATION_BEFORE_PRODUCE);
                messageBroker.produce(producedMessage, this);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
