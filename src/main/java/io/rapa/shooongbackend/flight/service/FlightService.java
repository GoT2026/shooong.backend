package io.rapa.shooongbackend.flight.service;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.util.PreConditions;
import io.rapa.shooongbackend.flight.dto.FlightReplayResponse;
import io.rapa.shooongbackend.flight.constant.FlightStatus;
import io.rapa.shooongbackend.flight.dto.FlightRecordRequest;
import io.rapa.shooongbackend.flight.dto.FlightRecordVo;
import io.rapa.shooongbackend.flight.dto.StartFlightResponse;
import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.flight.dto.postion.RotationRequest;
import io.rapa.shooongbackend.flight.entity.Flights;
import io.rapa.shooongbackend.flight.repository.FlightRepository;
import io.rapa.shooongbackend.flightrecord.entity.FlightRecords;
import io.rapa.shooongbackend.flightrecord.repository.FlightRecordRepository;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import io.rapa.shooongbackend.ranking.dto.LeaderboardResultResponse;
import io.rapa.shooongbackend.ranking.util.LeaderboardScores;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class FlightService {

    private final FlightRepository flightRepository;
    private final OrderRepository orderRepository;
    private final FlightRecordRepository flightRecordRepository;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public StartFlightResponse startFlight(Long orderId){
        Orders founded = orderRepository.findByIdOrThrow(orderId);



        founded.getFlights().stream().forEach(
                (flight) -> PreConditions.validate(
                        !flight.getFlightStatus().equals(FlightStatus.IN_FLIGHT),
                        ErrorCode.ALREADY_ASSIGNED_ORDER
                )
        );

        return StartFlightResponse.from(
                flightRepository.save(
                        new Flights(founded)
                )
        );
    }


    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void setCrashed(Long flightId){
        Flights founded = flightRepository.findByIdOrThrow(flightId);
        founded.setCrashed();
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public LeaderboardResultResponse setCompleted(Long flightId){
        Flights founded = flightRepository.findByIdOrThrow(flightId);
        founded.setCompleted();

        List<FlightRecords> records = flightRecordRepository.findAllByFlightOrderBySequenceAsc(founded);
        Long totalFlightTime = calculateTotalFlightTime(records);
        Double averageTilt = calculateAverageTilt(records);
        Double angleScore = LeaderboardScores.angleScore(averageTilt);
        Double timeScore = LeaderboardScores.timeScore(totalFlightTime);
        Double totalScore = angleScore + timeScore;

        Orders order = founded.getOrder();
        order.updateLeaderboardScore(
                totalScore,
                totalFlightTime,
                averageTilt
        );

        Long rank = orderRepository.countRankingAhead(totalScore, totalFlightTime) + 1;
        order.updateRating(rank);

        return LeaderboardResultResponse.of(
                rank,
                founded,
                angleScore,
                timeScore
        );
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void recordFlights(
            FlightRecordRequest request
    ){
        Flights founded = flightRepository.findByIdOrThrow(request.flightId());

        PreConditions.validate(
                founded.getFlightStatus().equals(FlightStatus.IN_FLIGHT),
                ErrorCode.CAN_NOT_RECORD
        );

        List<FlightRecords> list = request.samples().stream().map(
                (record) -> FlightRecords.builder()
                        .timeStamp(Instant.ofEpochMilli(record.timestampUnixMs()))
                        .positionRequest(record.position())
                        .rotationRequest(record.rotation())
                        .checkpointRequest(record.checkpoint())
                        .sequence(record.sequence())
                        .timestampUnixMs(record.timestampUnixMs())
                        .elapsedTimeSeconds(record.elapsedTimeSeconds())
                        .flight(founded)
                        .build()
        ).toList();
        founded.addAllFlightRecord(list);
        flightRecordRepository.saveAll(list);
    }

    @PreAuthorize("isAuthenticated()")
    public FlightReplayResponse getReplay(Long flightId) {
        Flights founded = flightRepository.findByIdOrThrow(flightId);

        List<FlightRecordVo> samples = flightRecordRepository.findAllByFlightOrderBySequenceAsc(founded)
                .stream()
                .map(record -> new FlightRecordVo(
                        record.getSequence(),
                        record.getTimestampUnixMs(),
                        record.getElapsedTimeSeconds(),
                        new PositionRequest(
                                record.getPositions().getPosX(),
                                record.getPositions().getPosY(),
                                record.getPositions().getPosZ()
                        ),
                        new RotationRequest(
                                record.getRotations().getRotX(),
                                record.getRotations().getRotY(),
                                record.getRotations().getRotZ(),
                                record.getRotations().getRotW()
                        ),
                        record.getCheckpoint().toRequest()
                ))
                .toList();

        return FlightReplayResponse.of(founded, samples);
    }

    private Long calculateTotalFlightTime(List<FlightRecords> records) {
        if (records.isEmpty()) {
            return 0L;
        }

        Double elapsedTimeSeconds = records.get(records.size() - 1).getElapsedTimeSeconds();
        if (elapsedTimeSeconds != null && elapsedTimeSeconds > 0) {
            return (long) Math.ceil(elapsedTimeSeconds);
        }

        Long start = records.get(0).getTimestampUnixMs();
        Long end = records.get(records.size() - 1).getTimestampUnixMs();
        return (long) Math.ceil(Math.max(0L, end - start) / 1000.0);
    }

    private Double calculateAverageTilt(List<FlightRecords> records) {
        if (records.isEmpty()) {
            return 0.0;
        }

        return records.stream()
                .mapToDouble(record -> (
                        Math.abs(record.getCoordinate().getRoll())
                                + Math.abs(record.getCoordinate().getPitch())
                ) / 2)
                .average()
                .orElse(0.0);
    }
}
