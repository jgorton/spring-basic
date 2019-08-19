package com.github.jgorton.demo;

class OrderNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  OrderNotFoundException(Long id) {
    super("Could not find order " + id);
  }
}