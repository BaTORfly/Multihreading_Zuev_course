package ru.batorfly.producer;

import ru.batorfly.model.Message;

public class MessageFactory{
    private static final int INITIAL_NEXT_MESSAGE_INDEX = 1;
    private static final String CREATED_MESSAGE_TEMPLATE = "Message #%d";

    private int nextMessageIndex;

    public MessageFactory() {
        this.nextMessageIndex = INITIAL_NEXT_MESSAGE_INDEX;
    }

    public Message createAndReturnMessage() {
        return new Message(String.format(CREATED_MESSAGE_TEMPLATE, increaseNextMessageIndex()));
    }

    /**
     * Так как все три потока-производителя будут использовать одну фабрику,
     * необходимо обеспечить потокобезопасность.
     * Операция инкремента не является атомарным, поэтому его нужно вынести в отдельный
     * синхронизированный метод.
     */
    private synchronized int increaseNextMessageIndex() {
        return this.nextMessageIndex++;
    }
}
