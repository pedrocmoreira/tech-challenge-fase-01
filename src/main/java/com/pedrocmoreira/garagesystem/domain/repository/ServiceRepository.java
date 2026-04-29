package com.pedrocmoreira.garagesystem.domain.repository;

import com.pedrocmoreira.garagesystem.domain.model.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository {
    Service save(Service service);
    Optional<Service> findById(Long id);
    List<Service> listAll();
    List<Service> listActives();
    void delete(Long serviceId);
}
