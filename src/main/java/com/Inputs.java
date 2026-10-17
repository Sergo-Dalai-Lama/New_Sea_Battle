package com;

import java.io.IOException;

public interface Inputs {
    ShipSetting input() throws IOException;
    SetShipCommand inputSetShips()  throws IOException ;
    UnSetShipCommand inputUnSetShip() throws IOException;
    AttackCommand inputAttack() throws IOException;
}
