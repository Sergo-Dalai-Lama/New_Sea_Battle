package com;

import com.core.*;

import java.io.IOException;

public interface Inputs {
    ShipSetting input() throws IOException;
    SetShipCommand inputSetShips()  throws IOException ;
    UnSetShipCommand inputUnSetShip() throws IOException;
    AttackCommand inputAttack() throws IOException;
    RepositionShipCommand inputRepositionShip() throws IOException;
}
