package com.github.jgorton.demo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {

  private final EmployeeRepository repository;

  private final EmployeeResourceAssembler assembler;

  EmployeeController(EmployeeRepository repository, EmployeeResourceAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  // Aggregate root

  // // no links with this version of the method
  // @GetMapping("/employees")
  // List<Employee> all() {
  //   return repository.findAll();
  // }

  @GetMapping("/employees")
  Resources<Resource<Employee>> all() {

    List<Resource<Employee>> employees = repository.findAll().stream()
      .map(assembler::toResource)
      .collect(Collectors.toList());

    return new Resources<>(employees,
      linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
  }

  @PostMapping("/employees")
  ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
    Resource<Employee> resource = assembler.toResource(repository.save(newEmployee));

    return ResponseEntity
      .created(new URI(resource.getId().expand().getHref()))
      .body(resource);
  }
  // Employee newEmployee(@RequestBody Employee newEmployee) {
  //   return repository.save(newEmployee);
  // }

  // Single item

  // // no linking with this one
  // @GetMapping("/employees/{id}")
  // Employee one(@PathVariable Long id) {

  //   return repository.findById(id)
  //     .orElseThrow(() -> new EmployeeNotFoundException(id));
  // }

  @GetMapping("/employees/{id}")
  Resource<Employee> one(@PathVariable Long id) {

  Employee employee = repository.findById(id)
    .orElseThrow(() -> new EmployeeNotFoundException(id));

  return assembler.toResource(employee);
}

  @PutMapping("/employees/{id}")
  Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

    return repository.findById(id)
      .map(employee -> {
        employee.setName(newEmployee.getName());
        employee.setRole(newEmployee.getRole());
        return repository.save(employee);
      })
      .orElseGet(() -> {
        newEmployee.setId(id);
        return repository.save(newEmployee);
      });
  }

  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);
  }
}