package com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper;

import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.CustomerEntity;

public final class CustomerMapper {
    private CustomerMapper() {}

    public  static Customer toDomain(CustomerEntity entity) {
        if(entity == null) return null;
        return Customer.builder()
                .id(entity.getId())
                .document(entity.getDocument())
                .documentType(entity.getDocumentType() == null ? null
                        : Customer.DocumentType.valueOf(entity.getDocumentType().name()))
                .name(entity.getName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .build();
    }


    public static CustomerEntity toEntity(Customer domain) {
        if (domain == null) return null;
        return CustomerEntity.builder()
                .id(domain.getId())
                .document(domain.getDocument())
                .documentType(domain.getDocumentType() == null ? null
                        :CustomerEntity.DocumentType.valueOf(domain.getDocumentType().name()))
                .name(domain.getName())
                .phone(domain.getPhone())
                .email(domain.getEmail())
                .build();
    }
}
