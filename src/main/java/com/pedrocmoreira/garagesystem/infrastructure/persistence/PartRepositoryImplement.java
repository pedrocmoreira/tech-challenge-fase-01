package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.Part;
import com.pedrocmoreira.garagesystem.domain.repository.PartRepository;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper.PartMapper;
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
        return PartMapper.toDomain(jpa.save(PartMapper.toEntity(part)));
    }

    @Override
    public Optional<Part> filterById(Long id) {
        return jpa.findById(id).map(PartMapper::toDomain);
    }

    @Override
    public List<Part> listAll() {
        return jpa.findAll().stream().map(PartMapper::toDomain).toList();
    }

    @Override
    public List<Part> listActives() {
        return jpa.findByActiveTrue().stream().map(PartMapper::toDomain).toList();
    }

    @Override
    public List<Part> listWithCriticStock() {
        return jpa.findCriticStock().stream().map(PartMapper::toDomain).toList();
    }

    @Override
    public void delete(Long id) {
        jpa.deleteById(id);
    }
}