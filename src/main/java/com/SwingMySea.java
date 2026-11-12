package com;

import com.core.GameConfig;
import com.displayModel.MySeaDisplayModel;

import javax.swing.*;
import java.awt.*;

import static com.ColorBattleGame.*;
import static com.ColorBattleGame.BORDER_CELL;

public class SwingMySea extends JPanel {
    private MySeaDisplayModel model;
    public SwingMySea(MySeaDisplayModel model) {
        this.model = model;
        setLayout(null);
        setBounds(
                SwingConfig.WIDTH_SEA+SwingConfig.WIDTH_SEA/SwingConfig.SCALE_MY_SEA/2,
                SwingConfig.HEIGHT/2,
                SwingConfig.WIDTH_SEA/SwingConfig.SCALE_MY_SEA,
                SwingConfig.HEIGHT_SEA/SwingConfig.SCALE_MY_SEA);
        setVisible(true);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            paintMySea(g2d);
        } finally {
            g2d.dispose();
        }
    }
    private void paintMySea(Graphics g) {
        Graphics2D  g2d = (Graphics2D) g;

        for (int i = 0; i< GameConfig.ROWS; i++) {
            for (int j = 0; j<GameConfig.COLUMNS; j++) {
                switch (model.getType(i,j)) {
                    case HIT:
                        g2d.setColor(HIT);
                        break;
                    case MISS:
                        g2d.setColor(MISS);
                        break;
                    case DECK:
                        g2d.setColor(DECK);
                        break;
                    case SEA:
                        g2d.setColor(FILLED_CELL);
                }
                g2d.fillRect(
                        i*SwingConfig.WIDTH_DECK/SwingConfig.SCALE_MY_SEA,
                        j*SwingConfig.HEIGHT_DECK/SwingConfig.SCALE_MY_SEA,
                        SwingConfig.WIDTH_DECK/SwingConfig.SCALE_MY_SEA,
                        SwingConfig.HEIGHT_DECK/SwingConfig.SCALE_MY_SEA);
            }
        }
        g2d.setColor(BORDER_CELL);
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0;i<=SwingConfig.HEIGHT_SEA/SwingConfig.SCALE_MY_SEA;i+=SwingConfig.HEIGHT_DECK/SwingConfig.SCALE_MY_SEA){
            g2d.drawLine(0,i,SwingConfig.WIDTH_SEA/SwingConfig.SCALE_MY_SEA,i);
        }
        for (int i = 0;i<=SwingConfig.WIDTH_SEA/SwingConfig.SCALE_MY_SEA;i+=SwingConfig.WIDTH_DECK/SwingConfig.SCALE_MY_SEA){
            g2d.drawLine(i,0,i,SwingConfig.HEIGHT_SEA/SwingConfig.SCALE_MY_SEA);
        }
    }
}

