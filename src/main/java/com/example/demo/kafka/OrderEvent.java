package com.example.demo.kafka;

import java.io.Serializable;

public record OrderEvent(
        String parcelNumber,
        String emailReceiver,
        String receiverCountryCode,
        String senderCountryCode,
        int statusCode
) implements Serializable {
}
