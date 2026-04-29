package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImplement implements CustomerRepository {
    private final CustomerJpaRepository jpa;

    @Override
    public Customer save(Customer customer) {
        return jpa.save(customer);
    }

    @Override
    public Optional<Customer> filterById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Customer> filterByDocument(String document) {
        return jpa.findByDocument(document);
    }

    @Override
    public List<Customer> listAll() {
        return jpa.findAll();
    }

    @Override
    public void delete(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByDocument(String document) {
        return jpa.existsByDocument(document);
    }

}
