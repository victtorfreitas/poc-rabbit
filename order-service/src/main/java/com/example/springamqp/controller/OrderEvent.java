package com.example.springamqp.controller;

import java.math.BigDecimal;


public class OrderEvent {

  private Long id;
  private BigDecimal valor;

  public OrderEvent() {
  }

  public OrderEvent(Long id, BigDecimal valor) {
    this.id = id;
    this.valor = valor;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }
}
