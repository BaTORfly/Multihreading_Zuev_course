package ru.batorfly.broker;

import ru.batorfly.model.Message;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageBroker {
    private static final int CAPACITY = 5;
    private final Queue<Message> messagesToBeConsumed;

    public MessageBroker() {
        this.messagesToBeConsumed = new ArrayDeque<>(CAPACITY);
    }

    public synchronized void produce(final Message message){
        try{
            // пока достигнут лимит вметимости брокера, засыпаем, ждем
            while (this.messagesToBeConsumed.size() >= CAPACITY){
                super.wait();
            }
            // пока место есть, кладем сообщение в брокер
            this.messagesToBeConsumed.add(message);
            // пробуждаем поток для потребления
            super.notify();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Message consume(){
        try {
            // пока в брокере пусто, спим, ждем появления сообщений
            while (this.messagesToBeConsumed.isEmpty()){
                super.wait();
            }
            // пока сообщения есть, потребляем
            Message consumedMessage = this.messagesToBeConsumed.poll();
            // потребили, пробуждаем поток для производства, так как гарантировано появилось место
            super.notify();
            return consumedMessage;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
