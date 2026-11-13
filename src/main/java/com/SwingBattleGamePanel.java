package com;

import com.core.GameRules;
import com.core.ListShip;
import com.displayModel.EnemySeaDisplayModel;
import com.displayModel.MySeaDisplayModel;
import javax.swing.*;
import java.awt.*;

public class SwingBattleGamePanel extends JPanel {
    private final GameRules gameRules;
    private final ListShip listShip;
    public SwingBattleGamePanel(ListShip listShip, GameRules gameRules) {
        this.listShip = listShip;
        this.gameRules = gameRules;
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH, SwingConfig.HEIGHT);
        int y = (SwingConfig.HEIGHT - SwingConfig.HEIGHT_SEA)/2;
        setOpaque(false);
        EnemySeaDisplayModel model = new EnemySeaDisplayModel();
        SwingEnemySea enemySea = new SwingEnemySea(model);
        SwingMySea mySea = new SwingMySea(new MySeaDisplayModel(listShip));
        add(enemySea);
        add(mySea);
        enemySea.setLocation(0, y);
        ControllerBattle controllerBattle = new ControllerBattle(this,enemySea,mySea,gameRules);
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
