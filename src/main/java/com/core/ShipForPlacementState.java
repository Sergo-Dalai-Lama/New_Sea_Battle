package com.core;

public class ShipForPlacementState {
    public final Ship ship;
    public final Coordinate coordinate;
    public final Orientation orientation;

    public ShipForPlacementState(Ship ship, Coordinate coordinate, Orientation orientation) {
        this.ship = ship;
        this.coordinate = coordinate;
        this.orientation = orientation;
    }
    @Override
    public String toString() {
        return ship+", "+coordinate+", "+orientation+"\n";
    }
}
