package com;

import com.core.GameRules;

import java.awt.*;

public class ControllerStartButton implements StartBattleButton.StartButtonListener {
    private final Container contentPane;
    private final GameRules gameRules;
    public  ControllerStartButton(Container contentPane,  GameRules gameRules) {
        this.gameRules = gameRules;
        this.contentPane = contentPane;
    }

    @Override
    public void onStartBattleButton() {
        contentPane.removeAll();
        contentPane.revalidate();
        contentPane.repaint();
        SwingBattleGamePanel swingBattleGamePanel = new SwingBattleGamePanel(contentPane,gameRules);
    }
}
