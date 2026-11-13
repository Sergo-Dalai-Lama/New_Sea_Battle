package com;

import com.core.Coordinate;
import com.core.GameRules;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ControllerBattle implements SwingEnemySea.EnemySeaMouseListener{
    private final SwingBattleGamePanel gamePanel;
    private final SwingEnemySea enemySea;
    private final SwingMySea mySea;
    private final GameRules gameRules;
    private Coordinate newCoordinate = null;
    public ControllerBattle(SwingBattleGamePanel gamePanel,SwingEnemySea enemySea, SwingMySea mySea,  GameRules gameRules) {
        this.gamePanel = gamePanel;
        this.enemySea = enemySea;
        this.mySea = mySea;
        this.gameRules = gameRules;
        enemySea.setEnemySeaMouseListener(this);

    }

    @Override
    public void onClick(MouseEvent event) {

    }

    @Override
    public void onMoved(MouseEvent event) {
        Coordinate coordinate = pointToCoordinate(event.getPoint());
        if (!coordinate.equals(newCoordinate)){
            newCoordinate=coordinate;
            enemySea.setHoveredCell(coordinate);
            enemySea.revalidate();
            enemySea.repaint();
        }
    }

    @Override
    public void onExted(MouseEvent event) {
        enemySea.resetHoveredCell();
        enemySea.revalidate();
        enemySea.repaint();
    }

    private Coordinate pointToCoordinate(Point point){
        return new Coordinate(point.x/SwingConfig.WIDTH_DECK, point.y/SwingConfig.HEIGHT_DECK);
    }
    private Point coordinateToPoint(Coordinate coordinate){
        return new Point(coordinate.x()*SwingConfig.WIDTH_DECK, coordinate.y()*SwingConfig.HEIGHT_DECK);
    }


}
