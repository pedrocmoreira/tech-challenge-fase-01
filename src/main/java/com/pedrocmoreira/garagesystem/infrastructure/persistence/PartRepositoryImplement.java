package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.Part;
import com.pedrocmoreira.garagesystem.domain.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PartRepositoryImplement implements PartRepository {
    private final PartJpaRepository jpa;


    @Override
    public Part save(Part part) {
        return jpa.save(part);
    }

    @Override
    public Optional<Part> filterById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Part> listAll() {
        return jpa.findAll();
    }

    @Override
    public List<Part> listActives() {
        return jpa.findByActiveTrue();
    }

    @Override
    public List<Part> listWithCriticStock() {
        return jpa.findCriticStock();
    }

    @Override
    public void delete(Long id) {
        jpa.deleteById(id);
    }
}
