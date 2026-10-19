package com;

import java.util.Set;

public record AttackResult(
        Coordinate coordinate,
        boolean hit,
        boolean targetDestroyed,
        Set<Coordinate> targetsCoordinates,
        AttackOutcome outcome,
        int scoreEarned
) {
}
