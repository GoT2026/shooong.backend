package io.rapa.shooongbackend.ranking.dto;

import io.rapa.shooongbackend.flight.entity.Flights;
import io.rapa.shooongbackend.order.entity.Orders;
import lombok.Builder;

@Builder
public record LeaderboardResultResponse(
        Long rank,
        Long orderId,
        Long flightId,
        String userName,
        Double angleScore,
        Double timeScore,
        Double totalScore,
        Long totalFlightTime,
        Double averageTilt,
        String orderStatus
) {
    public static LeaderboardResultResponse of(
            Long rank,
            Flights flight,
            Double angleScore,
            Double timeScore
    ) {
        Orders order = flight.getOrder();

        return LeaderboardResultResponse.builder()
                .rank(rank)
                .orderId(order.getOrderId())
                .flightId(flight.getFlightId())
                .userName(order.getMember().getName())
                .angleScore(angleScore)
                .timeScore(timeScore)
                .totalScore(order.getScore())
                .totalFlightTime(order.getTotalFlightTime())
                .averageTilt(order.getAverageTilt())
                .orderStatus(order.getOrderStatus().name())
                .build();
    }
}
