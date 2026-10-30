package com;

import com.core.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static com.ColorStartGame.*;

public class SwingStartGameSea extends JPanel {
    private final ListShip listShip;
    private final GameRules gameRules;
    private final List<Point> points = new ArrayList<Point>();
    private final boolean[][] preCanSetResult = new boolean[GameConfig.ROWS][GameConfig.COLUMNS];
    public SwingStartGameSea(ListShip listShip, GameRules gameRules) {
        this.listShip = listShip;
        this.gameRules = gameRules;
        generatePreCanSetResultTrue();
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH_SEA, SwingConfig.HEIGHT_SEA);
        generatePoints();
    }

    private void paintSeaBattle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i<GameConfig.ROWS; i++) {
            for (int j = 0; j<GameConfig.COLUMNS; j++) {
                if(!preCanSetResult[i][j]) {
                    g2d.setColor(NON_SET_SHIP);
                } else {g2d.setColor(FILLED_CELL);}
                g2d.fillRect(i*SwingConfig.WIDTH_DECK,j*SwingConfig.HEIGHT_DECK,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);
            }
        }
        g2d.setColor(BORDER_CELL);
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0;i<=SwingConfig.HEIGHT_SEA;i+=SwingConfig.HEIGHT_DECK){
            g2d.drawLine(0,i,SwingConfig.WIDTH_SEA,i);
        }
        for (int i = 0;i<=SwingConfig.WIDTH_SEA;i+=SwingConfig.WIDTH_DECK){
            g2d.drawLine(i,0,i,SwingConfig.HEIGHT_SEA);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            paintSeaBattle(g2d);
        } finally {
            g2d.dispose();
        }
    }
    private void generatePoints() {
        for (int i = 0; i< GameConfig.ROWS;i++){
            for (int j = 0; j< GameConfig.COLUMNS;j++){
                Point p = new Point(i*SwingConfig.WIDTH_DECK,j*SwingConfig.HEIGHT_DECK);
                points.add(p);
            }
        }
    }

    public SetShipResult setShip(SetShipCommand command) {
        return  gameRules.setShip(command);
    }
    public void unSetShip(UnSetShipCommand command) {
        gameRules.unSetShip(command);
    }
    public List<Point> getPoints() {
        return List.copyOf(points);
    }
    public void paintByResult(SetShipResult result, Orientation orientation,Ship ship) {

            if (result.outcome()!=SetShipOutcome.SET) {
               if (orientation == Orientation.HORIZONTAL) {
                   for (int i = 0; i < ship.getSize(); i++) {
                       if (i+result.coordinate().x()<GameConfig.ROWS) {
                           preCanSetResult[i+result.coordinate().x()][result.coordinate().y()] = false;
                       }
                   }
               } else {
                   for (int i = 0; i < ship.getSize(); i++) {
                       if (i+result.coordinate().y()<GameConfig.COLUMNS) {
                           preCanSetResult[result.coordinate().x()][i+result.coordinate().y()] = false;
                       }
                   }
               }
           }
            repaint();
    }

    public void generatePreCanSetResultTrue() {
        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLUMNS; j++) {
                preCanSetResult[i][j] = true;
            }
        }
    }
}
