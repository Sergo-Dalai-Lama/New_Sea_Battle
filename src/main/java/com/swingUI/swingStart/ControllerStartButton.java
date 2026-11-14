package com.swingUI.swingStart;

import com.swingUI.swingBattle.SwingBattleGamePanel;
import com.core.GameRules;
import com.core.ListShip;

public class ControllerStartButton implements StartBattleButton.StartButtonListener {
    private final GameRules gameRules;
    private final SwingStartGameFrame frame;
    private final ListShip listShip;
    public  ControllerStartButton(ListShip listShip, GameRules gameRules, SwingStartGameFrame frame) {
        this.gameRules = gameRules;
        this.frame = frame;
        this.listShip = listShip;
    }

    @Override
    public void onStartBattleButton() {
        frame.getContentPane().removeAll();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        SwingBattleGamePanel swingBattleGamePanel = new SwingBattleGamePanel(frame,listShip, gameRules);
        frame.getContentPane().add(swingBattleGamePanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
}
