package io.rapa.shooongbackend.ranking.dto;

import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.ranking.util.LeaderboardScores;
import lombok.Builder;

@Builder
public record RankingResponse(
        Long rank,
        Long orderId,
        String userName,
        Double angleScore,
        Double timeScore,
        Double totalScore,
        Long totalFlightTime,
        Double averageTilt,
        String orderStatus
) {
    public static RankingResponse of(Long rank, Orders order) {
        return RankingResponse.builder()
                .rank(rank)
                .orderId(order.getOrderId())
                .userName(order.getMember().getName())
                .angleScore(LeaderboardScores.angleScore(order.getAverageTilt()))
                .timeScore(LeaderboardScores.timeScore(order.getTotalFlightTime()))
                .totalScore(order.getScore())
                .totalFlightTime(order.getTotalFlightTime())
                .averageTilt(order.getAverageTilt())
                .orderStatus(order.getOrderStatus().name())
                .build();
    }
}
