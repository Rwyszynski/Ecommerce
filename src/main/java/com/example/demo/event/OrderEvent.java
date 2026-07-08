package com.example.demo.event;

import java.io.Serializable;

public record OrderEvent(
        String parcelNumber,
        String emailReceiver,
        String receiverCountryCode,
        String senderCountryCode,
        int statusCode
) implements Serializable {
}
