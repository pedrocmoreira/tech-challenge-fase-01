package com.pedrocmoreira.garagesystem.presentation.dto;

import java.math.BigDecimal;

public record EmailItemDTO (
    String name,
    Integer quantity,
    BigDecimal subtotal
    ){}
