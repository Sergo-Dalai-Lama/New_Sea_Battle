package com;

import java.util.Set;

public class MySeaDispayModel {
    private final ListShip shipList;
    private TypeCellMySea[][] mySea;
    public MySeaDispayModel(ListShip shipList) {
        this.shipList = shipList;
        fieldGenerate();
    }

    private void fieldGenerate() {
        mySea = new TypeCellMySea[GameConfig.ROWS][GameConfig.COLUMNS];
        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLUMNS; j++) {
                Coordinate currentCoordinate = new Coordinate(i, j);
                for (Coordinate coordinates : shipList.getValue()){
                    if (coordinates.equals(currentCoordinate)) {
                        mySea[i][j] = TypeCellMySea.DECK;
                        break;
                    } else {
                        mySea[i][j] = TypeCellMySea.SEA;
                    }
                }
            }
        }
    }

    public TypeCellMySea getType(int x, int y) {
        return mySea[x][y];
    }//вспомогательный метод для отображения

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

    public void applySetShipResult(SetShipResult result) {//метод должен быть во view, перенести, когда начну реализовывать консольный ввод-вывод
        if (result.outcome()==SetShipOutcome.SET){
            System.out.println("Set Ship Finished");
        }
        if (result.outcome()==SetShipOutcome.SHIP_COLLISION){
            System.out.println("Ship Collision!");
        }
        if (result.outcome()==SetShipOutcome.INVALID_DATA){
            System.out.println("Invalid Data!");
        }
        if (result.outcome()==SetShipOutcome.BEYOND_FIELD){
            System.out.println("Beyond Field!");
        }
    }

}
