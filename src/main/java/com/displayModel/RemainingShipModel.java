package com.displayModel;

import com.core.*;

import java.util.ArrayList;
import java.util.List;

public class RemainingShipModel {//класс для создания модели списка оставшихся кораблей
    //по сути аналог My/EnemySeaDisplayModel,
    //поэтому дублирует то, что есть в ListShip, но необходим для разделения ViewModel от GameCore/Model
    private final List<Ship> mySeaRemainingSetShipFromList;
    private final List<Ship[]> mySeaRemainingSetShipModel =  new ArrayList<Ship[]>();
    public RemainingShipModel() {
        this.mySeaRemainingSetShipFromList = List.of(Ship.values());
        generateShipsListDisplay();
    }

    public List<Ship[]> getMySeaRemainingSetShipModel() {
        return mySeaRemainingSetShipModel;
    }

    private void generateShipsListDisplay(){

        int k = 4;//надо будет убирать магическое число и заменить на глобальное "количество типов кораблей"
        for (int i = 0; i < k; i++) {//цикл по количеству кораблей в зависимости от типа (всего 4 типа)
            Ship[] listOfShipsOfType = new Ship[i+1];//создали список длиной в кол. кор. в зависимости от типа
            int l = i*(i+1)/2;
            for (int j = l; j < l+i+1; j++) {
                Ship currentShip = mySeaRemainingSetShipFromList.get(j);
                listOfShipsOfType[j-l] = currentShip;
            }
            mySeaRemainingSetShipModel.add(listOfShipsOfType);
        }
    }

    private void addShipToModel(Ship ship) {//вернуть корабль в список remaining
        //переписать потом вместе с enum Ship, начинается колхоз из-за того, что
        //корабли и типы смешаны
        int i = mySeaRemainingSetShipFromList.indexOf(ship);//вернули индекс корабля
        int x = (int)(Math.sqrt(8*i+1)-1)/2;//вычислили номер типа корабля
        int y = i-(x*(x+1))/2;//вычислили порядковый номер корабля
        Ship[] currentShip = mySeaRemainingSetShipModel.get(x);//вернули список кораблей одного типа
        currentShip[y] = ship;//добавили корабль в список
        mySeaRemainingSetShipModel.add(currentShip);
    }

    public void applySetShipResult(SetShipResult setShipResult) {
        if (setShipResult.outcome()== SetShipOutcome.SET){
            for (Ship[] ships : mySeaRemainingSetShipModel) {//цикл по типам кораблей
                for (int j = 0; j < ships.length; j++) {//цикл по кораблям типа
                    if (ships[j] != null && ships[j].equals(setShipResult.deliveredShip())) {//если клетка не пустая и
                        //совпадает корабль, то за null ить его
                        ships[j] = null;
                        break;
                    }
                }
            }
        }
    }

    public void applyUnSetShipResult(UnSetShipResult unSetShipResult) {
        if (unSetShipResult.unSetShipOutcome()== UnSetShipOutcome.UNSET){
            for (Ship ships : mySeaRemainingSetShipFromList) {//цикл по типам кораблей
                if (ships.equals(unSetShipResult.ship())) {
                    addShipToModel(unSetShipResult.ship());
                    break;
                }
            }
        }
    }
}
