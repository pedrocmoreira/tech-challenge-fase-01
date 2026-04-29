package com.pedrocmoreira.garagesystem.domain.repository;

import com.pedrocmoreira.garagesystem.domain.model.Part;

import java.util.List;
import java.util.Optional;

public interface PartRepository {
    Part save(Part part);
    Optional<Part> filterById(Long id);
    List<Part> listAll();
    List<Part> listActives();
    List<Part> listWithCriticStock();
    void delete(Long id);
}
