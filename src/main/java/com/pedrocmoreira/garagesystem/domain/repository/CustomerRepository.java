package com.pedrocmoreira.garagesystem.domain.repository;

import com.pedrocmoreira.garagesystem.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> filterById(Long id);
    Optional<Customer> filterByDocument(String document);
    List<Customer> listAll();
    void delete(Long id);
    boolean existsByDocument(String document);
}
