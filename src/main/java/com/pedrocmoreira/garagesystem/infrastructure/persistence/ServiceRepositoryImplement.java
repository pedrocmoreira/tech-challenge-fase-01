package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.Service;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class ServiceRepositoryImplement  implements ServiceRepository {
    private final ServiceJpaRepository jpa;

    @Override
    public Service save(Service service) {
        return jpa.save(service);
    }

    @Override
    public Optional<Service> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Service> listAll() {
        return jpa.findAll();
    }

    @Override
    public List<Service> listActives() {
        return jpa.findByActiveTrue();
    }

    @Override
    public void delete(Long serviceId) {
        jpa.deleteById(serviceId);
    }
}
