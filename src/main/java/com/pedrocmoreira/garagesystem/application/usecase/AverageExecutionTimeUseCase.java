package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class AverageExecutionTimeUseCase {
    private final ServiceOrderRepository serviceOrderRepository;

    public record AverageTimeResult (
            long totalCompletedServiceOrders,
            Double avarageTimeInMinutes,
            Double avarageTimeInHours
    ){}

    @Transactional(readOnly = true)
    public AverageTimeResult execute(){
        List<ServiceOrder> completed = serviceOrderRepository.listByStatus(StatusSO.FINALIZADA);
        List<ServiceOrder> delivered = serviceOrderRepository.listByStatus(StatusSO.ENTREGUE);

        List<Long> times = java.util.stream.Stream
                .concat(completed.stream(), completed.stream())
                .map(ServiceOrder::getExecutionTimeInMinutes)
                .filter(t -> t != null && t > 0)
                .toList();

        if(times.isEmpty()) {
            return new AverageTimeResult(0, null, null);
        }

        OptionalDouble average = times.stream()
                .mapToLong(Long::longValue)
                .average();

        double averageMinutes = average.orElse(0);

        return new AverageTimeResult(
                times.size(),
                averageMinutes,
                averageMinutes / 60
        );
    }
}
