package ru.batorfly.broker;

import ru.batorfly.consumer.MessageConsumingTask;
import ru.batorfly.model.Message;
import ru.batorfly.producer.MessageProducingTask;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.function.DoubleFunction;

public class MessageBroker {

    private static final String EOL = System.lineSeparator();
    private static final String MESSAGE_PRODUCED_TEMPLATE = "Message '%s' is produced by producer '%s'" +
            "Amount of messages before producing: %d." + EOL;
    private static final String MESSAGE_CONSUMED_TEMPLATE = "Message '%s' is consumed by consumer '%s'" +
            "Amount if messages before consuming: %d." + EOL;

    public static final int CAPACITY = 15;
    private final Queue<Message> messagesToBeConsumed;

    public MessageBroker() {
        this.messagesToBeConsumed = new ArrayDeque<>(CAPACITY);
    }

    public synchronized void produce(final Message message, final MessageProducingTask producingTask){
        try{
            // пока достигнут лимит вметимости брокера, засыпаем, ждем
            while (!this.isShouldProduce(producingTask)){
                super.wait();
            }
            // пока место есть, кладем сообщение в брокер
            this.messagesToBeConsumed.add(message);
            // пробуждаем поток для потребления
            System.out.printf(MESSAGE_PRODUCED_TEMPLATE, message, producingTask.getName(),
                    this.messagesToBeConsumed.size() - 1);
            super.notify();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> consume(final MessageConsumingTask consumingTask){
        try {
            // пока
            while (!this.isShouldConsume(consumingTask)){
                super.wait();
            }
            // пока сообщения есть, потребляем
            Message consumedMessage = this.messagesToBeConsumed.poll();
            // потребили, пробуждаем поток для производства, так как гарантировано появилось место
            System.out.printf(MESSAGE_CONSUMED_TEMPLATE, consumedMessage, consumingTask.getName(),
                    messagesToBeConsumed.size() + 1);
            super.notify();
            return Optional.ofNullable(consumedMessage);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    private boolean isShouldConsume(final MessageConsumingTask consumingTask){
        return !this.messagesToBeConsumed.isEmpty() &&
                this.messagesToBeConsumed.size() >= consumingTask.getMinimalAmountMessagesToConsume();

    }

    private boolean isShouldProduce(final MessageProducingTask producingTask){
        return this.messagesToBeConsumed.size() < this.CAPACITY
                && this.messagesToBeConsumed.size() <= producingTask.getMaximalAmountMessagesToProduce();
    }
}
