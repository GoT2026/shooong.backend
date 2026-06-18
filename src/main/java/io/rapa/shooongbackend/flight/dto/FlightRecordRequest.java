package io.rapa.shooongbackend.flight.dto;

import java.util.List;

public record FlightRecordRequest(
        Long flightId,
        List<FlightRecordVo> samples
) {
}
