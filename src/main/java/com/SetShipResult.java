package com;

public record SetShipResult(
        Coordinate coordinate,
        SetShipOutcome outcome,
        Ship deliveredShip
) {
}
