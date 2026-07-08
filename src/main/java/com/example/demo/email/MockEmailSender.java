package com.example.demo.email;

import com.example.demo.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockEmailSender {

    public void send(OrderEvent event) {
        if (event.parcelNumber() != null && event.parcelNumber().startsWith("FAIL")) {
            throw new RuntimeException("Mock email sender failed for parcelNumber=" + event.parcelNumber());
        }

        log.info("[MOCK EMAIL] To: {} | Parcel: {} | Status: {} | Route: {} -> {}",
                event.emailReceiver(),
                event.parcelNumber(),
                event.statusCode(),
                event.senderCountryCode(),
                event.receiverCountryCode());
    }
}
