package com.example.springamqp.consumer;

import java.math.BigDecimal;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CashBackConsumer {

  @RabbitListener(queues = "orders.v1.order-created.generate-cashback")
  public void eventListener(OrderEvent orderEvent) {
    System.out.println("Id recebido " + orderEvent.getId());
    System.out.println("Valor recebido " + orderEvent.getValor());
    if (orderEvent.getValor().compareTo(BigDecimal.valueOf(0)) < 0) {
      throw new RuntimeException();
    }
  }
}
