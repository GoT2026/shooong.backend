package io.rapa.shooongbackend.flightrecord.entity;

import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckPointPosition {
    @Column(nullable = false)
    private Double chk_posX;
    @Column(nullable = false)
    private Double chk_posY;
    @Column(nullable = false)
    private Double chk_posZ;

    public CheckPointPosition(Double x, Double y, Double z) {
        this.chk_posX = x;
        this.chk_posY = y;
        this.chk_posZ = z;
    }

    public static CheckPointPosition from(PositionRequest request){
        return new CheckPointPosition(request.x(), request.y(), request.z());
    }
}
