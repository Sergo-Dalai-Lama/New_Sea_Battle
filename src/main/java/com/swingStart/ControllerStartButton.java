package com.swingStart;

import com.SwingBattleGamePanel;
import com.core.GameRules;
import com.core.ListShip;

import javax.swing.*;
import java.awt.*;

public class ControllerStartButton implements StartBattleButton.StartButtonListener {
    private final Container contentPane;
    private final GameRules gameRules;
    private final JFrame frame;
    private final ListShip listShip;
    public  ControllerStartButton(Container contentPane, ListShip listShip, GameRules gameRules, JFrame frame) {
        this.gameRules = gameRules;
        this.contentPane = contentPane;
        this.frame = frame;
        this.listShip = listShip;
    }

    @Override
    public void onStartBattleButton() {
        contentPane.removeAll();
        contentPane.revalidate();
        contentPane.repaint();
        SwingBattleGamePanel swingBattleGamePanel = new SwingBattleGamePanel(listShip, gameRules);
        frame.getContentPane().add(swingBattleGamePanel);
        contentPane.revalidate();
        contentPane.repaint();
    }
}
