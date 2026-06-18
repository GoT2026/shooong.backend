package io.rapa.shooongbackend.flightrecord.entity;


import io.rapa.shooongbackend.common.util.EulerAngle;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coordinate {
    @Column(nullable = false)
    private Double roll;
    @Column(nullable = false)
    private Double pitch;
    @Column(nullable = false)
    private Double yaw;

    @Builder
    private Coordinate(Double roll, Double pitch, Double yaw) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public static Coordinate from(Rotation rotation){

        double[] coord = EulerAngle.quaternionToEuler(
                rotation.getRotX(),
                rotation.getRotY(),
                rotation.getRotZ(),
                rotation.getRotW()
        );

        return Coordinate.builder()
                .roll(coord[0])
                .pitch(coord[1])
                .yaw(coord[2])
                .build();
    }
}
