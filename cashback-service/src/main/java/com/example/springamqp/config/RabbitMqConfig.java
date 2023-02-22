package com.example.springamqp.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  @Bean
  public Queue queue() {
    Map<String, Object> args = new HashMap<>();
    args.put("x-dead-letter-exchange", "orders.v1.order-created.dlx");
//    args.put("x-dead-letter-routing-key", "orders.v1.order-created.dlx.generate-cashback.dlq");
//    return QueueBuilder.durable("orders.v1.order-created.generate-cashback")
//        .deadLetterExchange("orders.v1.order-created.dlx").build();
    return new Queue("orders.v1.order-created.generate-cashback",
        true, false, false, args);
  }

  @Bean
  public Binding binding() {
    Queue queue = queue();
    FanoutExchange exchange = new FanoutExchange("orders.v1.order-created");
    return BindingBuilder.bind(queue).to(exchange);
  }

  @Bean
  public Queue queueDLQ() {
    return new Queue("orders.v1.order-created.dlx.generate-cashback.dlq");
  }

  @Bean
  public Queue queueDLQParkingLot() {
    return new Queue("orders.v1.order-created.dlx.generate-cashback.dlq.parting-lot ");
  }

  @Bean
  public Binding bindingDLQ() {
    Queue queue = queueDLQ();
    FanoutExchange exchange = new FanoutExchange("orders.v1.order-created.dlx");
    return BindingBuilder.bind(queue).to(exchange);
  }

  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(
      RabbitAdmin rabbitAdmin) {
    return event -> {
      rabbitAdmin.initialize();
    };
  }

  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
      Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
    final var rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
    return rabbitTemplate;
  }
}
