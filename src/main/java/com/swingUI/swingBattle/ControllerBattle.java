package com.swingUI.swingBattle;

import com.core.*;
import com.swingUI.SwingConfig;
import com.swingUI.swingFinish.ShowDialogFinish;
import com.swingUI.swingStart.SwingStartGameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

public class ControllerBattle implements SwingEnemySea.EnemySeaMouseListener {
    private final SwingBattleGamePanel gamePanel;
    private final SwingEnemySea enemySea;
    private final SwingMySea mySea;
    private final GameRules gameRules;
    private Coordinate newCoordinate = null;
    private final Bot bot;
    private final SwingScore swingScore;
    private final SwingStartGameFrame frame;
    private boolean isPlayerTurn = true;
    public ControllerBattle(SwingStartGameFrame frame,SwingBattleGamePanel gamePanel, SwingEnemySea enemySea, SwingMySea mySea, GameRules gameRules, Bot bot, SwingScore swingScore) {
        this.gamePanel = gamePanel;
        this.enemySea = enemySea;
        this.mySea = mySea;
        this.gameRules = gameRules;
        this.bot = bot;
        this.swingScore = swingScore;
        this.frame = frame;
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
                    swingScore.scoreUpdate(bot.getGameRules().getScore());
                }
                if (attackResult.outcome() == AttackOutcome.MISS) {
                    isPlayerTurn = false;
                    enemySea.blockAttack();
                }
                if (attackResult.outcome() == AttackOutcome.GAME_FINISHED) {
                    String text = "You win!";
                    ShowDialogFinish showDialogFinish = new ShowDialogFinish(bot.getGameRules().getScore(),frame,text);
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
                String text = "You lost(";
                ShowDialogFinish showDialogFinish = new ShowDialogFinish(bot.getGameRules().getScore(),frame,text);
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
        return new Coordinate(point.x/ SwingConfig.WIDTH_DECK, point.y/SwingConfig.HEIGHT_DECK);
    }

}
