package io.rapa.shooongbackend.order.dto;

import io.rapa.shooongbackend.order.entity.Orders;
import lombok.Builder;

@Builder
public record OrderDetailsResponse(
    Long orderId,
    Long rating,
    Double score,
    Long totalFlightTime,
    Double averageTilt,
    String orderStatus,
    Integer remainWaypointCnt,
    Boolean isCrashed
) {
    public static OrderDetailsResponse from(Orders orders){
        return OrderDetailsResponse.builder()
                .orderId(orders.getOrderId())
                .rating(orders.getRating())
                .score(orders.getScore())
                .totalFlightTime(orders.getTotalFlightTime())
                .averageTilt(orders.getAverageTilt())
                .orderStatus(orders.getOrderStatus().name())
                .remainWaypointCnt(orders.getRemainWaypointCnt())
                .isCrashed(orders.getIsCrashed())
                .build();
    }
}
