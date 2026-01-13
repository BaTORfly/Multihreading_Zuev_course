package ru.batorfly;

import ru.batorfly.broker.MessageBroker;
import ru.batorfly.consumer.MessageConsumingTask;
import ru.batorfly.producer.MessageFactory;
import ru.batorfly.producer.MessageProducingTask;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        var messageBroker = new MessageBroker();
        var messageFactory = new MessageFactory();

        var firstProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory,
                MessageBroker.CAPACITY, "PRODUCER_1"));
        var secondProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory,
                10, "PRODUCER_2"));
        var thirdProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory,
                5, "PRODUCER_3"));


        var firstConsumingThread = new Thread(new MessageConsumingTask(messageBroker,
                0, "CONSUMER_1"));
        var secondConsumingThread = new Thread(new MessageConsumingTask(messageBroker,
                6, "CONSUMER_2"));
        var thirdConsumingThread = new Thread(new MessageConsumingTask(messageBroker,
                11, "CONSUMER_3"));

        startThreads(
                firstConsumingThread,
                secondConsumingThread,
                thirdConsumingThread,
                firstProducingThread,
                secondProducingThread,
                thirdProducingThread);
    }

    private static void startThreads(Thread... threads){
        Arrays.stream(threads).forEach(Thread::start);
    }
}