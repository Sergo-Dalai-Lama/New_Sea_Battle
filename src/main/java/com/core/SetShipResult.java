package com.core;

public record SetShipResult(
        Coordinate coordinate,
        SetShipOutcome outcome,
        Ship deliveredShip
) {
}
