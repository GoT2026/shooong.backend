package io.rapa.shooongbackend.flightrecord.entity;

import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position {
    @Column(nullable = false)
    private Double posX;
    @Column(nullable = false)
    private Double posY;
    @Column(nullable = false)
    private Double posZ;

    public Position(Double x, Double y, Double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public static Position from(PositionRequest request){
        return new Position(request.x(), request.y(), request.z());
    }
}
