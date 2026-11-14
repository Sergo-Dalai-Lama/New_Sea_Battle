package com.swingUI.swingBattle;

import com.swingUI.SwingConfig;
import com.core.Bot;
import com.core.GameRules;
import com.core.ListShip;
import com.displayModel.EnemySeaDisplayModel;
import com.displayModel.MySeaDisplayModel;
import com.swingUI.swingStart.SwingStartGameFrame;
import javax.swing.*;
import java.awt.*;

public class SwingBattleGamePanel extends JPanel {
    private final GameRules gameRules;
    private final ListShip listShip;
    private final SwingStartGameFrame frame;
    public SwingBattleGamePanel(SwingStartGameFrame frame,ListShip listShip, GameRules gameRules) {
        this.listShip = listShip;
        this.gameRules = gameRules;
        this.frame = frame;
        Bot bot = new Bot();
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH, SwingConfig.HEIGHT);
        int y = (SwingConfig.HEIGHT - SwingConfig.HEIGHT_SEA)/2;
        setOpaque(false);
        EnemySeaDisplayModel model = new EnemySeaDisplayModel();
        SwingEnemySea enemySea = new SwingEnemySea(model);
        SwingMySea mySea = new SwingMySea(new MySeaDisplayModel(listShip));
        SurrenderButton surrenderButton = new SurrenderButton();
        SwingScore swingScore = new SwingScore();
        add(swingScore);
        add(surrenderButton);
        add(enemySea);
        add(mySea);
        enemySea.setLocation(0, y);
        ControllerBattle controllerBattle = new ControllerBattle(frame,this,enemySea,mySea,gameRules,bot,swingScore);
        JLabel textEnemy = new JLabel("This is Enemy sea");
        JLabel textMy = new JLabel("This is My sea");
        textEnemy.setFont(new Font("Colibri", Font.BOLD, 15));
        textMy.setFont(new Font("Colibri", Font.BOLD, 15));
        textEnemy.setVisible(true);
        textMy.setVisible(true);
        textEnemy.setBounds(0, 0, 150, 50);
        textMy.setBounds(0, 0, 150, 50);
        int xTextEnemy = SwingConfig.WIDTH_SEA/2-textEnemy.getWidth()/2;
        int yTextEnemy = 3*SwingConfig.HEIGHT/4+textEnemy.getHeight();
        int xTextMy = 3*SwingConfig.WIDTH/4-textMy.getWidth()/2;
        int yTextMy = yTextEnemy;
        add(textEnemy);
        add(textMy);
        textEnemy.setLocation(xTextEnemy,yTextEnemy);
        textMy.setLocation(xTextMy,yTextMy);
    }
}
