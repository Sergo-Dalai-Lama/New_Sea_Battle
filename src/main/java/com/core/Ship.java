package com.core;

public enum Ship {
    BATTLESHIP1(4),
    CRUISER1(3),
    CRUISER2(3),
    DESTROYER1(2),
    DESTROYER2(2),
    DESTROYER3(2),
    PATROL1(1),
    PATROL2(1),
    PATROL3(1),
    PATROL4(1);

    private int size;
    Ship(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }

}
