package ru.batorfly;

import ru.batorfly.broker.MessageBroker;
import ru.batorfly.consumer.MessageConsumingTask;
import ru.batorfly.producer.MessageProducingTask;

public class Main {
    public static void main(String[] args) {
        var messageBroker = new MessageBroker();

        var producingThread = new Thread(new MessageProducingTask(messageBroker));
        var consumingThread = new Thread(new MessageConsumingTask(messageBroker));

        producingThread.start();
        consumingThread.start();
    }
}