package com.core;

import java.util.*;

public class ListShip {
    private final EnumMap<Ship, Set<Coordinate>> allListShips = new EnumMap<>(Ship.class);
    private final EnumMap<Ship, Set<Coordinate>> deathShips = new EnumMap<>(Ship.class);
    public ListShip() {//я могу указать конструктор без аргументов, если мне нужен он, а я сделал
        //copy, то автоматически генерироваться не будет
    }

    public Set<Coordinate> getValue(){//возвращаю копию, чтобы не нарушать безопасность ядра
        Set<Coordinate> coordinates = new HashSet<>();
        for (var entry : allListShips.entrySet()) {
            coordinates.addAll(entry.getValue());
        }
        return coordinates;
    }

    public void clear(){
        allListShips.clear();
    }

    public Set<Coordinate> getDeathShip(Coordinate coordinate) {
        return Set.copyOf(
                deathShips.get(getShipDeathAtCoordinate(coordinate))//возвращаем не оригинальный список, а его копию, чтобы не нарушать инкапсуляцию,
                //иначе можно будет просто изменить эти данные снаружи, хотя ядро не должно меняться извне
        );
    }

    public boolean shipLiveAtCoordinate(Coordinate coordinate) {
        for (Set<Coordinate> coordinates : allListShips.values()) {
            if (coordinates.contains(coordinate)) return true;
        }
        return false;
    }

    public boolean isShipOccupied(Ship ship) {
        return (allListShips.get(ship) != null);
    }

    public boolean isShipOccupied(Coordinate coordinate) {
        return (allListShips.get(getShipLiveAtCoordinate(coordinate)) != null);
    }

    public Ship unSetShipInList(Coordinate coordinate) {
        Ship ship = getShipLiveAtCoordinate(coordinate);
        allListShips.remove(ship);
        return ship;
    }

    public boolean aliveIsShip(Coordinate coordinate) {//метод сделать приватным
        if (getShipLiveAtCoordinate(coordinate)==null) {//здесь обращение к all, корабля быть не должно
            return !allListShips.get(getShipDeathAtCoordinate(coordinate)).isEmpty();//здесь обращение к death по координате
        }
        return true;
    }

    public void addDeck(Ship ship, Coordinate coordinate) {
        allListShips.computeIfAbsent(ship, k -> new HashSet<>())
                .add(new Coordinate(coordinate.x(),coordinate.y()));
    }

    public void deleteDeck(Coordinate coordinate) {

        for (var entry : allListShips.entrySet()) {//вытащили весь список и прошлись по каждому элементу
            Ship ship = entry.getKey();//получили отдельный корабль
               Set<Coordinate> coordinates = entry.getValue();//получили его координаты
                    if (coordinates.contains(coordinate)) {//найти корабль с данной координатой
                        deathShips.computeIfAbsent(ship, k -> new HashSet<>())//скопировать координату и корабль
                                //в список поврежденных кораблей
                                .add(coordinate);
                        coordinates.remove(coordinate);//удалить координату
                        return;
                    }
        }
    }

    public int sizeLive(){
        int size = 0;
        for (var entry : allListShips.entrySet()) {
            Ship ship = entry.getKey();
            if (!allListShips.get(ship).isEmpty()) size++;
        }

        return size;
    }

    public int sizeDeath(){return deathShips.size();}

    private Ship getShipDeathAtCoordinate(Coordinate coordinate) {
        for (Ship ship : deathShips.keySet()) {
            if (deathShips.get(ship).contains(coordinate)) {
                return ship;
            }
        }
        return null;
    }

    private Ship getShipLiveAtCoordinate(Coordinate coordinate) {

        for (Ship ship : allListShips.keySet()) {
            if (allListShips.get(ship).contains(coordinate)) {
                return ship;
            }
        }
        return null;
    }

}
