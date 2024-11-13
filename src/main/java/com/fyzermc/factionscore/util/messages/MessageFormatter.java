package com.fyzermc.factionscore.util.messages;

import lombok.Getter;

public abstract class MessageFormatter<T> {

    @Getter
    protected final String prefix;

    protected MessageFormatter(String prefix) {
        this.prefix = prefix;
    }

    public String getMessage(String message) {
        return MessageUtils.translateColorCodes(this.prefix + message);
    }

    public abstract void send(T sender, String message);
}