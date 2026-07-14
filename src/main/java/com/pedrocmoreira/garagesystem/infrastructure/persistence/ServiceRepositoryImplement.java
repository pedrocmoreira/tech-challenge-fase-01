package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.Service;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceRepository;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ServiceRepositoryImplement implements ServiceRepository {
    private final ServiceJpaRepository jpa;

    @Override
    public Service save(Service service) {
        return ServiceMapper.toDomain(jpa.save(ServiceMapper.toEntity(service)));
    }

    @Override
    public Optional<Service> findById(Long id) {
        return jpa.findById(id).map(ServiceMapper::toDomain);
    }

    @Override
    public List<Service> listAll() {
        return jpa.findAll().stream().map(ServiceMapper::toDomain).toList();
    }

    @Override
    public List<Service> listActives() {
        return jpa.findByActiveTrue().stream().map(ServiceMapper::toDomain).toList();
    }

    @Override
    public void delete(Long serviceId) {
        jpa.deleteById(serviceId);
    }
}