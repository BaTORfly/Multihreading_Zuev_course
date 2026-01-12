package ru.batorfly.model;

import java.util.Objects;

public class Message {
    private final String data;

    public Message(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) return true;

        if (otherObj == null) return false;

        if (this.getClass() != otherObj.getClass()) return false;

        final Message other = (Message) otherObj;

        return Objects.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[data=" + this.data + "]";
    }
}
