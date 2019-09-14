package com.kwabenaberko.smsretrieverandroid;

public class SmsRetrievedEvent {
    private boolean timeout;
    private String smsMessage;

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }
}
