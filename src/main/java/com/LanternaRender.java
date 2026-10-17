package com;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class LanternaRender implements Render {
    TerminalSize size = new TerminalSize(60,50);
    DefaultTerminalFactory factory = new DefaultTerminalFactory().setInitialTerminalSize(size);

    Terminal terminal = factory.createTerminal();
    public Screen screen = new TerminalScreen(terminal);
    public TextGraphics textGraphics;
    private final char[] rowNameX = new char[GameConfig.ROWS];
    private final char[] rowNameY = new char[GameConfig.COLUMNS];
    private final char[] shipName = new char[4];
    public LanternaRender() throws IOException {

            screen.startScreen();//создали экран для рисования
            textGraphics = screen.newTextGraphics();
            screen.setCursorPosition(null);
        rowNameX[0]='a'; rowNameX[1]='b'; rowNameX[2]='c'; rowNameX[3]='d'; rowNameX[4]='e';
        rowNameX[5]='f'; rowNameX[6]='g'; rowNameX[7]='h'; rowNameX[8]='i'; rowNameX[9]='k';
        for (int j=0; j<GameConfig.COLUMNS; j++){rowNameY[j]=(char) (j+'0');}
        shipName[0]='b'; shipName[1]='c'; shipName[2]='d'; shipName[3]='p';
    }
    public void clear() {
        screen.clear();
    }
    public void close() throws IOException {
        screen.close();
        terminal.close();
    }
public void refresh() throws IOException {
        screen.refresh();
    }
@Override
public void renderingMySea(MySeaDispayModel mySeaDispayModel) throws IOException {
        int oy = 2;
        int ox = 1;
        for (int j = 0; j<GameConfig.ROWS; j++){
            for (int i = 0; i<GameConfig.COLUMNS; i++){
                if (mySeaDispayModel.getType(i,j)==TypeCellMySea.SEA) {
                    textGraphics.setForegroundColor(TextColor.ANSI.BLUE);
                    textGraphics.setCharacter(i+ox,j+oy,'⛝');
                }
                if (mySeaDispayModel.getType(i,j)==TypeCellMySea.MISS) {
                    textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
                    textGraphics.setCharacter(i+ox,j+oy,'⛝');
                }
                if (mySeaDispayModel.getType(i,j)==TypeCellMySea.DECK) {
                    textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                    textGraphics.setCharacter(i+ox,j+oy,'⛝');
                }
                if (mySeaDispayModel.getType(i,j)==TypeCellMySea.HIT) {
                    textGraphics.setForegroundColor(TextColor.ANSI.RED);
                    textGraphics.setCharacter(i+ox,j+oy,'⛝');
                }
            }
        }

    }
@Override
public void renderingEnemySea(EnemySeaDisplayModel enemySeaDisplayModel){
        int oy = 2;
        int ox = 15;
        for (int j = 0; j<GameConfig.ROWS; j++){
            for (int i = 0; i<GameConfig.COLUMNS; i++){
                if (enemySeaDisplayModel.getType(i,j)==TypeCellEnemy.UNKNOWN) {
                    textGraphics.setForegroundColor(TextColor.ANSI.BLUE);
                    textGraphics.setCharacter(i+ox,j+oy,'⛝');
                }
                if (enemySeaDisplayModel.getType(i,j)==TypeCellEnemy.MISS) {
                    textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
                    textGraphics.setCharacter(i+ox,j+oy,'⛝');
                }

                if (enemySeaDisplayModel.getType(i,j)==TypeCellEnemy.HIT) {
                    textGraphics.setForegroundColor(TextColor.ANSI.RED);
                    textGraphics.setCharacter(i+ox,j+oy,'⛝');
                }
            }
        }
    }
@Override
public void renderingMyBorder(){
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);

        screen.newTextGraphics().putString(2,0,"MY SEA");
        int ox = 1;
        int oy = 1;

        for (int i = 0; i<GameConfig.ROWS; i++){
            textGraphics.setCharacter(i+ox,oy,rowNameX[i]);
        }
        for (int j = 0;j<GameConfig.COLUMNS; j++){
            textGraphics.setCharacter(ox-1,j+oy+1,rowNameY[j]);
        }

    }
@Override
public void renderingEnemyBorder(){
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        screen.newTextGraphics().putString(16,0,"ENEMY SEA");
        int oyEnemy = 1;
        int oxEnemy = 15;

        for (int i = 0; i<GameConfig.ROWS; i++){
            textGraphics.setCharacter(i+oxEnemy,oyEnemy,rowNameX[i]);
        }
        for (int j = 0;j<GameConfig.COLUMNS; j++){
            textGraphics.setCharacter(oxEnemy-1,j+oyEnemy+1,rowNameY[j]);
        }

    }

@Override
public void renderGameOver(){
        textGraphics.setForegroundColor(TextColor.ANSI.RED);
        screen.newTextGraphics().putString(10,10,"Game Over");
    }
@Override
public void renderSetShips(ListShip listShip){//перенести потом во viewModel ядро метода
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        screen.newTextGraphics().putString(2,0,"MY SEA SET SHIP");
        int oy = 2;
        int ox = 1;
        for (int j = 0; j<GameConfig.ROWS; j++) {
            for (int i = 0; i < GameConfig.COLUMNS; i++) {
                Coordinate currentCoordinate = new Coordinate(i, j);
                textGraphics.setCharacter(i + ox, j + oy, '⚬');
                for (Coordinate coordinates : listShip.getValue()){
                    if (coordinates.equals(currentCoordinate)) {
                        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                        textGraphics.setCharacter(i+ox,j+oy,'⛝');
                        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
                    }
                }
            }
        }

        screen.newTextGraphics().putString(1,12,"SETTING SHIP");
        screen.newTextGraphics().putString(1,13, "Enter 'set' if you want set. Enter x,y,type,no.,orientation");
    screen.newTextGraphics().putString(1,14,"Enter 'unset' if you want unset. Enter x,y");
    screen.newTextGraphics().putString(1,15,"Enter 'move' if you want move. Enter x,y");
    screen.newTextGraphics().putString(1,16,"Enter 'rotate' if you want rotate.");
    }

@Override
public void renderingMyRemainingShip(RemainingShipModel remainingShipModel){
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        int ox = 15;
        int oy = 5;

        screen.newTextGraphics().putString(ox,oy,"MY REMAINING SHIP");
        List<Ship[]> mySeaRemainingSetShipModel = remainingShipModel.getMySeaRemainingSetShipModel();
        for (int i =0; i< mySeaRemainingSetShipModel.size();i++) {//цикл по типам кораблей
            int x = ox;
            for (int j = 0; j < mySeaRemainingSetShipModel.get(i).length; j++) {//цикл по кораблям типа
               for (int k = 0; k < 4-i; k++) {//цикл по бортам одного корабля
                   if (mySeaRemainingSetShipModel.get(i)[j] == null) {
                       textGraphics.setCharacter(k+x,  oy + 2+i, ' ');
                   } else {
                       textGraphics.setCharacter(k+x, oy + 2+i, '⛝');
                   }
               }
               x+=6-mySeaRemainingSetShipModel.get(i).length;
            }
        }
    }
}
