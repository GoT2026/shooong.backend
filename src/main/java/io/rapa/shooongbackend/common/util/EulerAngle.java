package io.rapa.shooongbackend.common.util;

public class EulerAngle {

    public static double[] quaternionToEuler(
            double x,
            double y,
            double z,
            double w
    ) {

        // Roll (X)
        double sinr = 2 * (w * x + y * z);
        double cosr = 1 - 2 * (x * x + y * y);
        double roll = Math.atan2(sinr, cosr);

        // Pitch (Y)
        double sinp = 2 * (w * y - z * x);

        double pitch;
        if (Math.abs(sinp) >= 1) {
            pitch = Math.copySign(Math.PI / 2, sinp);
        } else {
            pitch = Math.asin(sinp);
        }

        // Yaw (Z)
        double siny = 2 * (w * z + x * y);
        double cosy = 1 - 2 * (y * y + z * z);

        double yaw = Math.atan2(siny, cosy);

        return new double[]{
                Math.toDegrees(roll),
                Math.toDegrees(pitch),
                Math.toDegrees(yaw)
        };
    }
}