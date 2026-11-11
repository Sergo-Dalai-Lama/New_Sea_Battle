package com;

import com.core.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static com.ColorStartGame.*;

public class SwingStartGameSea extends JPanel {
    private final GameRules gameRules;
    private final List<Point> points = new ArrayList<Point>();
    private final PreviewSet[][] preCanSetResult = new PreviewSet[GameConfig.ROWS][GameConfig.COLUMNS];
    public SwingStartGameSea(GameRules gameRules) {
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
                switch (preCanSetResult[i][j]) {
                    case SET:
                        g2d.setColor(SET_SHIP);
                        break;
                    case NON_SET:
                        g2d.setColor(NON_SET_SHIP);
                        break;
                    case UNKNOWN:
                        g2d.setColor(FILLED_CELL);
                        break;
                }
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

    public List<Point> getPoints() {
        return List.copyOf(points);
    }

    public void paintByResult(Coordinate coordinate,Orientation orientation,Ship ship) {
        SetShipCommand command = new SetShipCommand(coordinate,ship,orientation);
        SetShipResult result = gameRules.setShip(command);
        if (result.deliveredShip() != null) {
            gameRules.unSetShip(new UnSetShipCommand(coordinate));
        }
            if (!(result.outcome()==SetShipOutcome.SET||result.outcome()==SetShipOutcome.SET_FINISHED)) {
               if (orientation == Orientation.HORIZONTAL) {
                   setPreCanSetResultHorizontal(result,coordinate,ship,PreviewSet.NON_SET);
               } else {
                   setPreCanSetResultVertical(result, coordinate, ship, PreviewSet.NON_SET);
               }
           } else {
                    if (orientation == Orientation.HORIZONTAL) {
                        setPreCanSetResultHorizontal(result, coordinate, ship, PreviewSet.SET);
                    } else {
                        setPreCanSetResultVertical(result, coordinate, ship, PreviewSet.SET);
                    }
            }
            repaint();
    }
public void newPaintByResult(Coordinate coordinate,Orientation orientation,ShipButton shipButton) {
    SetShipCommand command = new SetShipCommand(coordinate,shipButton.getShip(),orientation);
    SetShipResult result = gameRules.setShip(command);
    if (result.deliveredShip() != null) {
        gameRules.unSetShip(new UnSetShipCommand(coordinate));
    }
        if (!(result.outcome()==SetShipOutcome.SET||result.outcome()==SetShipOutcome.SET_FINISHED)) {
        if (orientation == Orientation.HORIZONTAL) {
            setPreCanSetResultHorizontal(result,coordinate,shipButton,PreviewSet.NON_SET);
        } else {
            setPreCanSetResultVertical(result, coordinate, shipButton, PreviewSet.NON_SET);
        }
    } else {
        if (orientation == Orientation.HORIZONTAL) {
            setPreCanSetResultHorizontal(result, coordinate, shipButton, PreviewSet.SET);
        } else {
            setPreCanSetResultVertical(result, coordinate, shipButton, PreviewSet.SET);
        }
    }
    repaint();
}
public void unSetShip(Coordinate coordinate) {

}

public void setShip(SetShipCommand command) {

}
public void generatePreCanSetResultTrue() {
        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLUMNS; j++) {
                preCanSetResult[i][j] = PreviewSet.UNKNOWN;
            }
        }
    }

    private void setPreCanSetResultHorizontal(SetShipResult result, Coordinate coordinate, Ship ship, PreviewSet previewSet) {
        for (int i = 0; i < ship.getSize(); i++) {
            if (i+result.coordinate().x()<GameConfig.ROWS) {
                preCanSetResult[i+result.coordinate().x()][result.coordinate().y()] = previewSet;
            }
        }
    }
    private void setPreCanSetResultVertical(SetShipResult result, Coordinate coordinate, Ship ship,PreviewSet previewSet) {
        for (int i = 0; i < ship.getSize(); i++) {
            if (i+result.coordinate().y()<GameConfig.COLUMNS) {
                preCanSetResult[result.coordinate().x()][i+result.coordinate().y()] = previewSet;
            }
        }
    }
    private void setPreCanSetResultHorizontal(SetShipResult result, Coordinate coordinate, ShipButton shipButton, PreviewSet previewSet) {
        for (int i = 0; i < shipButton.getShipSize(); i++) {
            if (i+result.coordinate().x()<GameConfig.ROWS) {
                preCanSetResult[i+result.coordinate().x()][result.coordinate().y()] = previewSet;
            }
        }
    }
    private void setPreCanSetResultVertical(SetShipResult result, Coordinate coordinate, ShipButton shipButton,PreviewSet previewSet) {
        for (int i = 0; i < shipButton.getShipSize(); i++) {
            if (i+result.coordinate().y()<GameConfig.COLUMNS) {
                preCanSetResult[result.coordinate().x()][i+result.coordinate().y()] = previewSet;
            }
        }
    }
}

