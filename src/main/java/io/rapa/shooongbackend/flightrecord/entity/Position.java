package io.rapa.shooongbackend.flightrecord.entity;

import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position {
    private Double posX;
    private Double posY;
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
