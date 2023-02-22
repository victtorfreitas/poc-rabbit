package com.example.springamqp.consumer;

public class OrderEvent {

  private Long id;
  private Double valor;

  public OrderEvent() {
  }

  public OrderEvent(Long id, Double valor) {
    this.id = id;
    this.valor = valor;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getValor() {
    return valor;
  }

  public void setValor(Double valor) {
    this.valor = valor;
  }
}
