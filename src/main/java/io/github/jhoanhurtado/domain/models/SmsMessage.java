package io.github.jhoanhurtado.domain.models;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;

public class SmsMessage implements MessageModel {
    private final String phoneNumber;
    private final String text;

    public SmsMessage(String phoneNumber, String text) {
        this.phoneNumber = phoneNumber;
        this.text = text;
    }

    @Override
    public String getDestination() {
        return phoneNumber;
    }

    @Override
    public String getContent() {
        return text;
    }

    @Override
    public String toJson() {
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }
}
