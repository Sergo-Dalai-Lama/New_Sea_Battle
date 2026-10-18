package com;

import java.util.Set;

public class MySeaDispayModel {
    private final ListShip listShip;
    private TypeCellMySea[][] mySea;
    public MySeaDispayModel(ListShip listShip) {
        this.listShip = listShip;
        fieldGenerate();
    }

    private void fieldGenerate() {
        mySea = new TypeCellMySea[GameConfig.ROWS][GameConfig.COLUMNS];
        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLUMNS; j++) {
                mySea[i][j] = TypeCellMySea.SEA;
                Coordinate currentCoordinate = new Coordinate(i, j);
                for (Coordinate coordinates : listShip.getValue()){
                    if (coordinates.equals(currentCoordinate)) {
                        mySea[i][j] = TypeCellMySea.DECK;
                        break;
                    }
                }
            }
        }
    }

    public TypeCellMySea getType(int x, int y) {
        return mySea[x][y];
    }

    public void applyAttackResultMySea(AttackResult result) {

        if (result.outcome()==AttackOutcome.INVALID_TARGET) {
            System.out.println("Invalid target");
            return;
        }

        if (result.hit()) {
            mySea[result.coordinate().x()][result.coordinate().y()]=TypeCellMySea.HIT;
            if (result.targetDestroyed()) {
                Set<Coordinate> coordinatesDeathShip = result.targetsCoordinates();
                for (Coordinate coordinate : coordinatesDeathShip) {
                    for (int j = -1; j < 2; j++) {
                        for (int k = -1; k < 2; k++) {
                            if (coordinate.x() + j >= 0
                                    && coordinate.x() + j < GameConfig.ROWS
                                    && coordinate.y() + k >= 0
                                    && coordinate.y() + k < GameConfig.COLUMNS) {
                                if ((mySea[coordinate.x() + j][coordinate.y() + k] == TypeCellMySea.SEA)) {
                                    mySea[coordinate.x() + j][coordinate.y() + k] = TypeCellMySea.MISS;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            mySea[result.coordinate().x()][result.coordinate().y()]=TypeCellMySea.MISS;
        }
    }

}
