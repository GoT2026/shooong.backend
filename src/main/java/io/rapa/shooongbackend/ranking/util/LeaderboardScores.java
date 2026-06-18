package io.rapa.shooongbackend.ranking.util;

public final class LeaderboardScores {
    private static final double MAX_ANGLE_SCORE = 50.0;
    private static final double MAX_TIME_SCORE = 50.0;
    private static final double ANGLE_PENALTY_PER_DEGREE = 1.0;
    private static final double TIME_PENALTY_PER_SECOND = 0.2;

    private LeaderboardScores() {
    }

    public static double angleScore(Double averageTilt) {
        if (averageTilt == null) {
            return 0.0;
        }
        return Math.max(0.0, MAX_ANGLE_SCORE - averageTilt * ANGLE_PENALTY_PER_DEGREE);
    }

    public static double timeScore(Long totalFlightTime) {
        if (totalFlightTime == null) {
            return 0.0;
        }
        return Math.max(0.0, MAX_TIME_SCORE - totalFlightTime * TIME_PENALTY_PER_SECOND);
    }
}
