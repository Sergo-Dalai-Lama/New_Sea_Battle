package com;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameCore {
    private final ListShip listShip;
    private final Set<Coordinate> blockedCells;//семантически правильнее использовать set, а не list.
    //у меня хранятся уникальные координаты
    public GameCore(ListShip listShip) {
        this.listShip = listShip;
        blockedCells = new HashSet<>();

    }

    public UnSetShipResult unSetShip(UnSetShipCommand unSetShipCommand) {
        Ship ship = null;
        if (listShip.getShipAvailable(unSetShipCommand.coordinate())){//если координат корабля нет в списке
            //возможных к установке, значит можно убрать его из списка установленных

            ship = listShip.unSetShipInList(unSetShipCommand.coordinate());
            return new UnSetShipResult(
                    ship,
                    UnSetShipOutcome.UNSET
            );
        }
        return new UnSetShipResult(
                ship,
                UnSetShipOutcome.NON_UNSET
        );
    }

    public SetShipResult setShip(SetShipCommand setShipCommand) {
        Ship ship = setShipCommand.ship();
        Coordinate coordinate = setShipCommand.coordinate();
        Orientation orientation = setShipCommand.orientation();

        if (listShip.getShipAvailable(ship)){//проверяем, есть ли такой тип кораблей еще

            return new SetShipResult(
                     coordinate,
                     SetShipOutcome.SHIP_IS_AVAILABLE,
                    null
             );
        }

        if (orientation == Orientation.HORIZONTAL) {
            if (beyondFieldHorizontal(coordinate, ship)) {
                return new SetShipResult(
                        coordinate,
                        SetShipOutcome.BEYOND_FIELD,
                        null
                );
            }
            if (shipCollisionHorizontal(coordinate,ship)) {
                return new SetShipResult(
                        coordinate,
                        SetShipOutcome.SHIP_COLLISION,
                        null
                );
            }
            for (int i = 0; i < ship.getSize(); i++) {
                Coordinate currentCoordinate = new Coordinate(i+coordinate.x(),coordinate.y());
                listShip.addDeck(ship,currentCoordinate);
            }

            if (listShip.sizeLive()==Ship.values().length){//проверяем, можно ли еще поставить корабли

                return new SetShipResult(
                        coordinate,
                        SetShipOutcome.SET_FINISHED,
                        null

                );
            }

            return new SetShipResult(
                    coordinate,
                    SetShipOutcome.SET,
                    ship
            );
        }
        else if (orientation == Orientation.VERTICAL) {
            if (beyondFieldVertical(coordinate,ship)) {
                return new SetShipResult(
                        coordinate,
                        SetShipOutcome.BEYOND_FIELD,
                        null
                );
            }
            if (shipCollisionVertical(coordinate,ship)) {
                return new SetShipResult(
                        coordinate,
                        SetShipOutcome.SHIP_COLLISION,
                        null
                );
            }
            for (int i = 0; i < ship.getSize(); i++) {
                Coordinate currentCoordinate = new Coordinate(coordinate.x(),coordinate.y()+i);
                listShip.addDeck(ship,currentCoordinate);
            }

            if (listShip.sizeLive()==Ship.values().length){//проверяем, можно ли еще поставить корабли

                return new SetShipResult(
                        coordinate,
                        SetShipOutcome.SET_FINISHED,
                        ship
                );
            }

            return new SetShipResult(
                    coordinate,
                    SetShipOutcome.SET,
                    ship
            );
        }

        return new SetShipResult(
                coordinate,
                SetShipOutcome.INVALID_DATA,
                null
        );

    }

    public AttackResult attack(AttackCommand attackCommand) {
        Coordinate coordinate = attackCommand.coordinate();

        if (!canAttack(coordinate)){
            return new AttackResult(
                    coordinate,
                    false,
                    false,
                    null,
                    AttackOutcome.INVALID_TARGET

            );
        }

        blockedCells.add(coordinate);

        if (listShip.shipLiveAtCoordinate(coordinate)){
            listShip.deleteDeck(coordinate);//перенесли борт корабля в поврежденные
            //здесь добавить возвращение списка координат убитого корабля
            //добавить результат - все корабли убиты, game over
            if (!listShip.aliveIsShip(coordinate)){
                Set<Coordinate> targets;

                targets = listShip.getDeathShip(coordinate);

                for (Coordinate target : targets) {
                    for (int j = -1; j < 2; j++) {
                        for (int k = -1; k < 2; k++) {
                            if (target.x() + j >= 0
                                    && target.x() + j < GameConfig.ROWS
                                    && target.y() + k >= 0
                                    && target.y() + k < GameConfig.COLUMNS) {
                                Coordinate temp = new Coordinate(target.x() + j, target.y() + k);
                                blockedCells.add(temp);//удалили проверку на входимость элемента,
                                // так как после замены List на Set теперь в координатах хранятся уникальные значения
                            }
                        }
                    }
                }
                if (listShip.sizeDeath()==Ship.values().length){//сначала проверить - все ли корабли убиты
                    return new AttackResult(
                            coordinate,
                            true,
                            true,
                            targets,
                            AttackOutcome.GAME_FINISHED
                    );
                }

                return new AttackResult(
                        coordinate,
                        true,
                        true,
                        targets,
                        AttackOutcome.DESTROYED

                );
            }
            return new AttackResult(
                    coordinate,
                    true,
                    false,
                    null,
                    AttackOutcome.HIT

            );
        } else {
            return new AttackResult(
                    coordinate,
                    false,
                    false,
                    null,
                    AttackOutcome.MISS

            );
        }

    }

    public void generationShipFormation(){

        List<Coordinate> includeCoordinates = new ArrayList<>();
        for (int i = 0; i <GameConfig.ROWS; i++) {//создали список всех координат
            for (int j = 0; j <GameConfig.COLUMNS; j++) {
                includeCoordinates.add(new Coordinate(i,j));
            }
        }

        List<Ship> includeShip = new ArrayList<>();
        Collections.addAll(includeShip, Ship.values());//создали список коралей аналогичный enum

        while (!(listShip.sizeLive()==Ship.values().length)){

            Ship ship = includeShip.get(ThreadLocalRandom.current().nextInt(includeShip.size()));
            Orientation orientation = Orientation.values()[ThreadLocalRandom.current().nextInt(Orientation.values().length)];

            boolean flag = true;
            while (flag) {
                if (includeCoordinates.isEmpty()) {
                    break;
                }
                Coordinate coordinate = includeCoordinates.get(ThreadLocalRandom.current().nextInt(includeCoordinates.size()));
                SetShipCommand setShipCommand = new SetShipCommand(coordinate, ship, orientation);
                SetShipResult result = this.setShip(setShipCommand);

                if (orientation == Orientation.HORIZONTAL) {//если горизонтальное положение
                    if (result.outcome() == SetShipOutcome.SET) {//проверить поставился ли корабль
                        //если да, то надо удалить координаты и корабль
                        removeCoordinatesBlock(includeCoordinates,coordinate,ship);
                        includeShip.remove(ship);
                        flag = false;

                    } else {//иначе поменять ориентацию и попытаться еще раз
                        setShipCommand = new SetShipCommand(coordinate, ship, Orientation.VERTICAL);
                        result = this.setShip(setShipCommand);
                        if (result.outcome() == SetShipOutcome.SET) {//проверить поставился ли корабль
                            //если да, то надо удалить координаты и корабль
                            removeCoordinatesBlock(includeCoordinates,coordinate,ship);
                            includeShip.remove(ship);
                            flag = false;
                        } else {//если оба варианта не прокатили - удалить координату
                            includeCoordinates.remove(coordinate);
                        }
                    }

                } else {
                    if (result.outcome() == SetShipOutcome.SET) {

                        removeCoordinatesBlock(includeCoordinates,coordinate,ship);
                        includeShip.remove(ship);
                        flag = false;

                    } else {//иначе поменять ориентацию и попытаться еще раз

                        setShipCommand = new SetShipCommand(coordinate, ship, Orientation.HORIZONTAL);
                        result = this.setShip(setShipCommand);

                        if (result.outcome() == SetShipOutcome.SET) {

                            removeCoordinatesBlock(includeCoordinates,coordinate,ship);
                            includeShip.remove(ship);
                            flag = false;

                        } else {
                            includeCoordinates.remove(coordinate);
                        }
                    }
                }
            }
        }
    }

    private boolean beyondFieldVertical(Coordinate coordinate, Ship ship) {
        return coordinate.y()<0||coordinate.y()+ship.getSize()>GameConfig.COLUMNS||coordinate.x()<0||coordinate.x()>GameConfig.ROWS;
    }

    private boolean beyondFieldHorizontal(Coordinate coordinate, Ship ship) {
        return coordinate.x()<0||coordinate.x()+ship.getSize()>GameConfig.ROWS||coordinate.y()<0||coordinate.y()>GameConfig.COLUMNS;
    }

    private boolean shipCollisionVertical(Coordinate coordinate, Ship ship) {
            for (int i = coordinate.x()-1; i <= coordinate.x() + 1; i++) {
                for (int j = coordinate.y() - 1; j < ship.getSize()+1+coordinate.y(); j++) {
                    Coordinate temp = new Coordinate(i, j);
                    if (listShip.shipLiveAtCoordinate(temp)) return true;
                }
            }
        return false;
   }

    private boolean shipCollisionHorizontal(Coordinate coordinate, Ship ship) {
            for (int i = coordinate.x() - 1; i < ship.getSize() + 1+coordinate.x(); i++) {
                for (int j = coordinate.y() - 1; j <= coordinate.y() + 1; j++) {
                    Coordinate temp = new Coordinate(i, j);
                    if (listShip.shipLiveAtCoordinate(temp)) return true;
                }
            }
        return false;
    }

    private boolean canAttack(Coordinate coordinate){

    if (blockedCells.contains(coordinate))return false;
    if (coordinate.x()<0||coordinate.x()>=GameConfig.ROWS||coordinate.y()<0||coordinate.y()>=GameConfig.COLUMNS) return false;
    return true;
}

    private void removeCoordinatesBlock(List<Coordinate> includeCoordinates, Coordinate coordinate, Ship ship){

    List<Coordinate> coordinatesBlockSet = new ArrayList<>();//создали пустой список координат, которые надо удалить
    for (int i = 0;i<ship.getSize();i++) {//заполнили нужными для удаления координатами
        coordinatesBlockSet.add(new Coordinate(coordinate.x()+i,coordinate.y()));
    }

    for (Coordinate coordinateBlock : coordinatesBlockSet) {//пробежались по всем координатам, которые надо удалить
        for (int j = -1; j < 2; j++) {
            for (int k = -1; k < 2; k++) {
                if (coordinateBlock.x() + j >= 0
                        && coordinateBlock.x() + j < GameConfig.ROWS
                        && coordinateBlock.y() + k >= 0
                        && coordinateBlock.y() + k < GameConfig.COLUMNS) {

                    includeCoordinates.remove(new Coordinate(coordinateBlock.x()+j,coordinateBlock.y()+k));//удалили эти координаты
                }
            }
        }
    }
}

}
