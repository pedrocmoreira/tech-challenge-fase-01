package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.repository.CustomerRepository;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper.CustomerMapper;
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
        return CustomerMapper.toDomain(jpa.save(CustomerMapper.toEntity(customer)));
    }

    @Override
    public Optional<Customer> filterById(Long id) {
        return jpa.findById(id).map(CustomerMapper::toDomain);
    }

    @Override
    public Optional<Customer> filterByDocument(String document) {
        return jpa.findByDocument(document).map(CustomerMapper::toDomain);
    }

    @Override
    public List<Customer> listAll() {
        return jpa.findAll().stream().map(CustomerMapper::toDomain).toList();
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