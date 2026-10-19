package com;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class LanternaInput implements Inputs{
    private final Screen screen;
    private final char[] rowNameX = new char[GameConfig.ROWS];
    private final char[] rowNameY = new char[GameConfig.COLUMNS];
    private final char[] shipName = new char[4];
    public LanternaInput(Screen screen) {
        this.screen = screen;
        rowNameX[0]='a'; rowNameX[1]='b'; rowNameX[2]='c'; rowNameX[3]='d'; rowNameX[4]='e';
        rowNameX[5]='f'; rowNameX[6]='g'; rowNameX[7]='h'; rowNameX[8]='i'; rowNameX[9]='k';
        for (int j=0; j<GameConfig.COLUMNS; j++){rowNameY[j]=(char) (j+'0');}
        shipName[0]='b'; shipName[1]='c'; shipName[2]='d'; shipName[3]='p';
    }
    @Override
    public ShipSetting input() throws IOException {

        StringBuilder input = new StringBuilder();
        while (true) {
            KeyStroke key = screen.readInput();

            if (key.getKeyType() == KeyType.Enter) {
                break;
            }

            if (key.getKeyType() == KeyType.Character) {
                input.append(key.getCharacter());
            }

            if (key.getKeyType() == KeyType.Backspace && !input.isEmpty()) {
                input.deleteCharAt(input.length() - 1);
            }
        }
        String inputstring = input.toString();

        if (inputstring.length() == 3) {
            if (inputstring.equals("set")) {
                return ShipSetting.SET;
            }
        }
        if (inputstring.length() == 5) {
            if (inputstring.equals("repos")) {
                return ShipSetting.REPOSITION;
            }
        }
        if (inputstring.length() == 5) {
            if (inputstring.equals("unset")) {
                return ShipSetting.UNSET;
            }
        }

        return ShipSetting.UNKNOWN;
    }
    @Override
    public SetShipCommand inputSetShips() throws IOException {
        StringBuilder input = new StringBuilder();
        while (true) {
            KeyStroke key = screen.readInput();

            if (key.getKeyType() == KeyType.Enter) {
                break;
            }

            if (key.getKeyType() == KeyType.Character) {
                input.append(key.getCharacter());
            }

            if (key.getKeyType() == KeyType.Backspace && !input.isEmpty()) {
                input.deleteCharAt(input.length() - 1);
            }
        }

        int x = -1;
        int y = -1;
        Ship ship = null;
        Orientation orientation = null;
        String inputString = input.toString();
        if (!(input.length() == 5)) {
            return new SetShipCommand(new Coordinate(x,y), ship, orientation);
        }
        for (int i =0; i<GameConfig.ROWS; i++){
            if (inputString.charAt(0)==rowNameX[i]){
                x = i;
            }
        }
        for (int i =0; i<GameConfig.COLUMNS; i++){
            if (inputString.charAt(1)==rowNameY[i]){
                y = i;
            }
        }

        if (inputString.charAt(4)=='h'){orientation=Orientation.HORIZONTAL;}
        if (inputString.charAt(4)=='v'){orientation=Orientation.VERTICAL;}
        for (int i =1; i<=4; i++){
            if (inputString.charAt(2)==shipName[i-1]){
                int numberShip = i+(int)inputString.charAt(3)-'0'-2;
                if (inputString.charAt(2)==shipName[2]){
                    numberShip = i+(int)inputString.charAt(3)-'0'-1;
                }
                if (inputString.charAt(2)==shipName[3]){
                    numberShip = i+(int)inputString.charAt(3)-'0'+1;
                }

                System.out.println(numberShip);

                if (numberShip<Ship.values().length){
                    ship = Ship.values()[numberShip];
                }
            }
        }
        System.out.println("x:"+x+", y:"+y);
        System.out.println(ship);
        System.out.println(orientation);
        Coordinate coordinate = new Coordinate(x,y);

        return new SetShipCommand(coordinate, ship, orientation);
    }

    @Override
    public UnSetShipCommand inputUnSetShip() throws IOException {

        StringBuilder input = new StringBuilder();
        while (true) {
            KeyStroke key = screen.readInput();

            if (key.getKeyType() == KeyType.Enter) {
                break;
            }

            if (key.getKeyType() == KeyType.Character) {
                input.append(key.getCharacter());
            }

            if (key.getKeyType() == KeyType.Backspace && !input.isEmpty()) {
                input.deleteCharAt(input.length() - 1);
            }
        }

        String inputString = input.toString();

        int x = -1;
        int y = -1;

        if (inputString.isEmpty()) {
            return new UnSetShipCommand(new Coordinate(x,y));
        }
        for (int i =0; i<GameConfig.ROWS; i++) {
            if (inputString.charAt(0)==rowNameX[i]) {
                x = i;
            }

        }
        for (int i =0; i<GameConfig.COLUMNS; i++) {
            if (inputString.charAt(1)==rowNameY[i]) {
                y = i;
            }
        }
        Coordinate coordinate = new Coordinate(x,y);
        return new UnSetShipCommand(coordinate);

    }
    @Override
    public RepositionShipCommand inputRepositionShip() throws IOException {
        StringBuilder input = new StringBuilder();
        while (true) {
            KeyStroke key = screen.readInput();

            if (key.getKeyType() == KeyType.Enter) {
                break;
            }

            if (key.getKeyType() == KeyType.Character) {
                input.append(key.getCharacter());
            }

            if (key.getKeyType() == KeyType.Backspace && !input.isEmpty()) {
                input.deleteCharAt(input.length() - 1);
            }
        }

        String inputString = input.toString();
        Orientation orientation = null;
        int x1 = -1;
        int y1 = -1;
        int x2 = -1;
        int y2 = -1;
        if (!(input.length() == 5)) {
            return new RepositionShipCommand(new Coordinate(x1,y1),new Coordinate(x2,y2),orientation);
        }

        for (int i =0; i<GameConfig.ROWS; i++) {
            if (inputString.charAt(0)==rowNameX[i]) {
                x1 = i;
            }

        }
        for (int i =0; i<GameConfig.COLUMNS; i++) {
            if (inputString.charAt(1)==rowNameY[i]) {
                y1 = i;
            }
        }
        for (int i =0; i<GameConfig.ROWS; i++) {
            if (inputString.charAt(2)==rowNameX[i]) {
                x2 = i;
            }

        }
        for (int i =0; i<GameConfig.COLUMNS; i++) {
            if (inputString.charAt(3)==rowNameY[i]) {
                y2 = i;
            }
        }
        if (inputString.charAt(4)=='h'){orientation=Orientation.HORIZONTAL;}
        if (inputString.charAt(4)=='v'){orientation=Orientation.VERTICAL;}

        Coordinate coordinate1 = new Coordinate(x1,y1);
        Coordinate coordinate2 = new Coordinate(x2,y2);
        return new RepositionShipCommand(coordinate1,coordinate2,orientation);
    }
    @Override
    public AttackCommand inputAttack() throws IOException {

        StringBuilder input = new StringBuilder();
        while (true) {
            KeyStroke key = screen.readInput();

            if (key.getKeyType() == KeyType.Enter) {
                break;
            }

            if (key.getKeyType() == KeyType.Character) {
                input.append(key.getCharacter());
            }

            if (key.getKeyType() == KeyType.Backspace && !input.isEmpty()) {
                input.deleteCharAt(input.length() - 1);
            }
        }
        int x = -1;
        int y = -1;
        String coordinateString = input.toString();
        for (int i =0; i<GameConfig.ROWS; i++){
            if (coordinateString.charAt(0)==rowNameX[i]){
                x = i;
            }
        }
        for (int i =0; i<GameConfig.COLUMNS; i++){
            if (coordinateString.charAt(1)==rowNameY[i]){
                y = i;
            }
        }
        System.out.println("x:"+x+", y:"+y);
        return new AttackCommand(new Coordinate(x,y));
    }
}
