package com;

import com.core.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class ControllerBattle implements SwingEnemySea.EnemySeaMouseListener{
    private final SwingBattleGamePanel gamePanel;
    private final SwingEnemySea enemySea;
    private final SwingMySea mySea;
    private final GameRules gameRules;
    private Coordinate newCoordinate = null;
    private final Bot bot;
    private final SwingScore  swingScore;
    private boolean isPlayerTurn = true;
    public ControllerBattle(SwingBattleGamePanel gamePanel,SwingEnemySea enemySea, SwingMySea mySea,  GameRules gameRules,Bot bot,SwingScore swingScore) {
        this.gamePanel = gamePanel;
        this.enemySea = enemySea;
        this.mySea = mySea;
        this.gameRules = gameRules;
        this.bot = bot;
        this.swingScore = swingScore;
        enemySea.setEnemySeaMouseListener(this);
    }

    @Override
    public void onClick(MouseEvent event) {
        if (isPlayerTurn) {
            enemySea.unBlockAttack();
            Coordinate coordinate = pointToCoordinate(event.getPoint());
            AttackCommand attackCommand = new AttackCommand(coordinate);
            AttackResult attackResult = bot.getGameRules().attack(attackCommand);
            enemySea.applyAttackResult(attackResult);
            if (attackResult.outcome()!=AttackOutcome.INVALID_TARGET) {
                if (attackResult.outcome() == AttackOutcome.HIT || attackResult.outcome() == AttackOutcome.DESTROYED) {
                    swingScore.scoreUpdate(attackResult);
                }
                if (attackResult.outcome() == AttackOutcome.MISS) {
                    isPlayerTurn = false;
                    enemySea.blockAttack();
                }
                if (attackResult.outcome() == AttackOutcome.GAME_FINISHED) {
                    //выводить сообщение о победе
                }
            }
        }
        if (!isPlayerTurn) {
            List<AttackResult> results = bot.start(gameRules);
            for (AttackResult  result : results) {
                mySea.applyAttackResult(result);
            }
            enemySea.unBlockAttack();
            isPlayerTurn = true;
            if (bot.getState()==GameState.WIN){
                //выводить сообщение о победе
            }
        }

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
