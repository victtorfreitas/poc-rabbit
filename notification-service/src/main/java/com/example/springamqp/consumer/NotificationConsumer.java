package com.example.springamqp.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

  @RabbitListener(queues = "orders.v1.order-created.send-notification")
  public void eventListener(OrderEvent orderEvent) {
    System.out.println("Id recebido " + orderEvent.getId());
    System.out.println("Valor recebido " + orderEvent.getValor());
  }
}
