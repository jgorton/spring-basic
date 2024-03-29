package com.github.jgorton.demo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
class Employee {

  private @Id @GeneratedValue Long id;
  // private String name;
  private String firstName;
  private String lastName;
  private String role;

  Employee() {}

  Employee(String name, String role) {
    setName(name);
    this.role = role;
  }

  Employee(String firstName, String lastName, String role) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
  }

  public String getName() {
    return this.firstName + " " + this.lastName;
  }

  public void setName(String name) {
    // assumes first and last are each one word with no spaces...
    String[] parts = name.split(" ");
    this.firstName = parts[0];
    this.lastName = parts[1];
  }
}