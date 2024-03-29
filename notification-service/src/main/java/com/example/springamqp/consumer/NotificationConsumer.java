package com.example.springamqp.consumer;

import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

  @RabbitListener(queues = "orders.v1.order-created.send-notification")
  public void eventListener(@NotNull OrderEvent orderEvent) {
    System.out.println("Id recebido " + orderEvent.getId());
    System.out.println("Valor recebido " + orderEvent.getValor());
    if (orderEvent.getValor() > 10) {
      throw new RuntimeException("Valor maior que permitido");
    }
  }
}
