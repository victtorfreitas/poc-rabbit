package com.example.springamqp.consumer;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueListener {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  private static final String X_RETRY_HEADER = "x-dlq-retry";
  private static final String DLQ = "orders.v1.order-created.dlx.generate-cashback.dlq";
  private static final String DLQ_PARKING_LOT = "orders.v1.order-created.dlx.generate-cashback.dlq.parting-lot ";

  @RabbitListener(queues = DLQ)
  public void process(OrderEvent orderEvent, @Headers Map<String, Object> headers) {
    Integer retryHeader = (Integer) headers.get(X_RETRY_HEADER);

    if (retryHeader == null) {
      retryHeader = 0;
    }
    final var id = orderEvent.getId();
    System.out.println("Reprocessing order by id " + id);
    if (retryHeader < 3) {
      Map<String, Object> updatedHeaders = new HashMap<>(headers);

      int tryCount = retryHeader + 1;
      updatedHeaders.put(X_RETRY_HEADER, tryCount);

      final MessagePostProcessor messagePostProcessor = message -> {
        final var messageProperties = message.getMessageProperties();
        updatedHeaders.forEach(messageProperties::setHeader);
        return message;

      };

      System.out.println(
          "Resend event id " + id + " retry number " + tryCount);
      this.rabbitTemplate.convertAndSend(DLQ, orderEvent, messagePostProcessor);
    } else {
      System.out.println("Send parking lot event id " + id);
      this.rabbitTemplate.convertAndSend(DLQ_PARKING_LOT, orderEvent);
    }
  }
}
