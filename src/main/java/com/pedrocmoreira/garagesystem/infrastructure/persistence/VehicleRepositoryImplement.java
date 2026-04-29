package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.Vehicle;
import com.pedrocmoreira.garagesystem.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleRepositoryImplement implements VehicleRepository {
    private final VehicleJpaRepository jpa;

    @Override
    public Vehicle save(Vehicle vehicle) {
        return jpa.save(vehicle);
    }

    @Override
    public Optional<Vehicle> filterById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Vehicle> filterByPlate(String plate) {
        return jpa.findByPlate(plate);
    }

    @Override
    public List<Vehicle> listByCustomer(Long customerId) {
        return jpa.findByCustomerId(customerId);
    }

    @Override
    public List<Vehicle> listAll() {
        return jpa.findAll();
    }

    @Override
    public void delete(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByPlate(String plate) {
        return jpa.existsByPlate(plate);
    }
}
