package com.swingUI.swingStart;

import com.core.GameRules;
import com.core.ListShip;
import com.swingUI.SwingConfig;

import javax.swing.*;
import java.awt.*;

import static com.swingUI.swingStart.ColorStartGame.BACKGROUND;

public class SwingStartGamePanel extends JPanel {
    private final GameRules gameRules;
    public final ListShip listShip;
    private final SwingStartGameFrame frame;
    public  SwingStartGamePanel(GameRules gameRules, ListShip listShip, SwingStartGameFrame frame) {
        this.gameRules = gameRules;
        this.listShip = listShip;
        this.frame = frame;
        setLayout(null);
        setSize(SwingConfig.WIDTH, SwingConfig.HEIGHT);
        setBackground(BACKGROUND);
        StartBattleButton startBattleButton = new StartBattleButton();
        add(startBattleButton);
        GenerateSetShipButton generateSetShipButton = new GenerateSetShipButton();
        add(generateSetShipButton);
        int y = (getHeight() - SwingConfig.HEIGHT_SEA) / 2;
        SwingStartGameSea sea = new SwingStartGameSea(gameRules);
        sea.setLocation(0, y);
        add(sea);
        SwingStartGameRemaining remaining = new SwingStartGameRemaining();
        int x = getWidth() - SwingConfig.WIDTH_SEA + SwingConfig.WIDTH_DECK;
        remaining.setLocation(x, y);
        add(remaining);
        ControllerPlacement placementController = new ControllerPlacement(sea, remaining,startBattleButton,generateSetShipButton,this,gameRules);
        remaining.bindController(placementController);
        ControllerStartButton startGame = new ControllerStartButton(listShip,gameRules,frame);
        startBattleButton.setStartButtonListener(startGame);
        setVisible(true);
    }
}
