package com;

import com.core.GameRules;
import com.displayModel.EnemySeaDisplayModel;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.*;

public class SwingBattleGamePanel extends JPanel {
    private GameRules gameRules;
    private Container contentPane;
    public SwingBattleGamePanel(Container contentPane, GameRules gameRules) {
        this.contentPane = contentPane;
        this.gameRules = gameRules;
        setLayout(null);
        setPreferredSize(new Dimension(SwingConfig.WIDTH, SwingConfig.HEIGHT));
        int y = (getHeight() - SwingConfig.HEIGHT_SEA)/2;
        setOpaque(false);
        EnemySeaDisplayModel model = new EnemySeaDisplayModel();
        SwingEnemySea enemySea = new SwingEnemySea(model);
        add(enemySea);
        enemySea.setLocation(0, y);
    }
}
