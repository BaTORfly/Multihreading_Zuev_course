package ru.batorfly.producer;

import ru.batorfly.broker.MessageBroker;
import ru.batorfly.model.Message;

import java.util.concurrent.TimeUnit;

public class MessageProducingTask  implements Runnable {
    private static final String EOL = System.lineSeparator();
    private static final String MESSAGE_PRODUCED_TEMPLATE = "Message '%s' is produced." + EOL;

    private static final int SECOND_DURATION_BEFORE_PRODUCE = 1;

    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;

    public MessageProducingTask(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
        this.messageFactory = new MessageFactory();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()){
                final Message producedMessage = messageFactory.createAndReturnMessage();
                TimeUnit.SECONDS.sleep(SECOND_DURATION_BEFORE_PRODUCE);
                messageBroker.produce(producedMessage);
                System.out.printf(MESSAGE_PRODUCED_TEMPLATE, producedMessage);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static final class MessageFactory{
        private static final int INITIAL_NEXT_MESSAGE_INDEX = 1;
        private static final String CREATED_MESSAGE_TEMPLATE = "Message #%d";

        private int nextMessageIndex;

        public MessageFactory() {
            this.nextMessageIndex = INITIAL_NEXT_MESSAGE_INDEX;
        }

        public Message createAndReturnMessage() {
            return new Message(String.format(CREATED_MESSAGE_TEMPLATE, this.nextMessageIndex++));
        }
    }
}
