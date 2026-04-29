package com.pedrocmoreira.garagesystem.domain.repository;

import com.pedrocmoreira.garagesystem.domain.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> filterById(Long id);
    Optional<Vehicle> filterByPlate(String plate);
    List<Vehicle> listByCustomer(Long customerId);
    List<Vehicle> listAll();
    void delete(Long id);
    boolean existsByPlate(String plate);
}
