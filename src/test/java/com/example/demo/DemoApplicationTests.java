package com.example.demo;

import com.example.demo.email.MockEmailSender;
import com.example.demo.event.OrderEvent;
import com.example.demo.kafka.KafkaTopicsConfig;
import com.example.demo.kafka.OrderEventConsumer;
import com.example.demo.kafka.OrderEventProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class DemoApplicationTests {

	@Test
	void shouldPublishOrderEventToKafkaTopic() {
		// given
		KafkaTemplate<String, OrderEvent> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
		KafkaTopicsConfig kafkaTopicsConfig = Mockito.mock(KafkaTopicsConfig.class);

		OrderEventProducer producer = new OrderEventProducer(kafkaTemplate, kafkaTopicsConfig);

		OrderEvent event = new OrderEvent(
				"PL123",
				"test@example.com",
				"PL",
				"DE",
				55
		);

		CompletableFuture<SendResult<String, OrderEvent>> future = new CompletableFuture<>();

		when(kafkaTopicsConfig.getOrderEventsTopic()).thenReturn("order-events");
		when(kafkaTemplate.send("order-events", "PL123", event)).thenReturn(future);

		// when
		producer.publish(event);

		// then
		verify(kafkaTemplate, times(1)).send("order-events", "PL123", event);
	}

	@Test
	void shouldConsumeOrderEventAndSendMockEmail() {
		// given
		MockEmailSender mockEmailSender = Mockito.mock(MockEmailSender.class);
		OrderEventConsumer consumer = new OrderEventConsumer(mockEmailSender);

		OrderEvent event = new OrderEvent(
				"PL123",
				"test@example.com",
				"PL",
				"DE",
				55
		);

		// when
		consumer.consume(event);

		// then
		verify(mockEmailSender, times(1)).send(event);
	}

	@Test
	void shouldThrowExceptionForFailParcelNumber() {
		// given
		MockEmailSender mockEmailSender = new MockEmailSender();

		OrderEvent event = new OrderEvent(
				"FAIL-001",
				"fail@example.com",
				"PL",
				"DE",
				55
		);

		// when / then
		assertThrows(RuntimeException.class, () -> mockEmailSender.send(event));
	}

	@Test
	void shouldSendMockEmailForValidParcelNumber() {
		// given
		MockEmailSender mockEmailSender = new MockEmailSender();

		OrderEvent event = new OrderEvent(
				"PL123",
				"test@example.com",
				"PL",
				"DE",
				55
		);

		// when / then
		assertDoesNotThrow(() -> mockEmailSender.send(event));
	}

}
