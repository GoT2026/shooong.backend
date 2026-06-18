package io.rapa.shooongbackend.flightrecord.entity;

import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.flight.dto.postion.RotationRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rotation {
    @Column(nullable = false)
    private Double rotX;
    @Column(nullable = false)
    private Double rotY;
    @Column(nullable = false)
    private Double rotZ;
    @Column(nullable = false)
    private Double rotW;

    public Rotation(Double x, Double y, Double z, Double w) {
        this.rotX = x;
        this.rotY = y;
        this.rotZ = z;
        this.rotW = w;
    }

    public static Rotation from(RotationRequest request){
        return new Rotation(request.x(), request.y(), request.z(), request.w());
    }
}
