package com.example.springamqp.consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NotificationConsumerTest {

  @Autowired
  private NotificationConsumer notificationConsumer;

  @Test
  @DisplayName("Should throw an exception when the value is greater than 10")
  void eventListenerWhenValueIsGreaterThan10ThenThrowException() {
    OrderEvent orderEvent = new OrderEvent(1L, 11D);
    assertThrows(RuntimeException.class, () -> notificationConsumer.eventListener(orderEvent));
  }
}
