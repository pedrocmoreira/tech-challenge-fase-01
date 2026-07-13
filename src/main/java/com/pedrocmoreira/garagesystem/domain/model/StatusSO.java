package com.pedrocmoreira.garagesystem.domain.model;

import com.pedrocmoreira.garagesystem.domain.exception.InvalidStatusTransitionException;

import java.util.Map;
import java.util.Set;

public enum StatusSO {
    RECEBIDA,
    EM_DIAGNOSTICO,
    AGUARDANDO_APROVACAO,
    EM_EXECUCAO,
    FINALIZADA,
    ENTREGUE,
    CANCELADA;

    private static final Map<StatusSO, Set<StatusSO>> ALLOWED_TRANSITIONS = Map.of(
            RECEBIDA,              Set.of(EM_DIAGNOSTICO),
            EM_DIAGNOSTICO,        Set.of(AGUARDANDO_APROVACAO),
            AGUARDANDO_APROVACAO,  Set.of(EM_EXECUCAO, CANCELADA),
            EM_EXECUCAO,           Set.of(FINALIZADA),
            FINALIZADA,            Set.of(ENTREGUE),
            ENTREGUE,              Set.of(),
            CANCELADA,             Set.of()
    );

    private static final Map<StatusSO, Integer> LISTING_PRIORITY = Map.of(
            EM_EXECUCAO, 0,
            AGUARDANDO_APROVACAO, 1,
            EM_DIAGNOSTICO, 2,
            RECEBIDA, 3
    );

    public StatusSO transitionTo(StatusSO nextStatus) {
        Set<StatusSO> allowedTransitions  = ALLOWED_TRANSITIONS.getOrDefault(this, Set.of());
        if (!allowedTransitions.contains(nextStatus)){
            throw  new InvalidStatusTransitionException(String.format("Transição inválida %s -> %s. Transições permitidas: %s", this, nextStatus, allowedTransitions));
        }
        return nextStatus;
    }

    public boolean canTransitionTo(StatusSO next) {
        return ALLOWED_TRANSITIONS.getOrDefault(this, Set.of()).contains(next);
    }

    public int listingPriority() {
        return LISTING_PRIORITY.getOrDefault(this, Integer.MAX_VALUE);
    }

}
