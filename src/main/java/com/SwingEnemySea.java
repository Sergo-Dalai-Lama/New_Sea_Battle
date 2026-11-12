package com;

import com.core.GameConfig;
import com.displayModel.EnemySeaDisplayModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ColorBattleGame.*;

public class SwingEnemySea extends JPanel {
    private EnemySeaDisplayModel  model;
    private final List<Point> points = new ArrayList<Point>();
    public SwingEnemySea(EnemySeaDisplayModel  model) {
        this.model = model;
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH_SEA, SwingConfig.HEIGHT_SEA);
        setVisible(true);
        generatePoints();
    }

    private void generatePoints() {
        for (int i = 0; i< GameConfig.ROWS; i++){
            for (int j = 0; j< GameConfig.COLUMNS;j++){
                Point p = new Point(i*SwingConfig.WIDTH_DECK,j*SwingConfig.HEIGHT_DECK);
                points.add(p);
            }
        }
    }

    public List<Point> getPoints() {
        return List.copyOf(points);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            paintEnemySea(g2d);
        } finally {
            g2d.dispose();
        }
    }

    private void paintEnemySea(Graphics g) {
         Graphics2D  g2d = (Graphics2D) g;

            for (int i = 0; i<GameConfig.ROWS; i++) {
                for (int j = 0; j<GameConfig.COLUMNS; j++) {
                    switch (model.getType(i,j)) {
                        case HIT:
                            g2d.setColor(HIT);
                            break;
                        case MISS:
                            g2d.setColor(MISS);
                            break;
                        case UNKNOWN:
                            g2d.setColor(UNKNOWN_CELL);
                            break;
                    }
                    g2d.fillRect(i*SwingConfig.WIDTH_DECK,j*SwingConfig.HEIGHT_DECK,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);
                }
            }
            g2d.setColor(BORDER_CELL);
            g2d.setStroke(new BasicStroke(1));
            for (int i = 0;i<=SwingConfig.HEIGHT_SEA;i+=SwingConfig.HEIGHT_DECK){
                g2d.drawLine(0,i,SwingConfig.WIDTH_SEA,i);
            }
            for (int i = 0;i<=SwingConfig.WIDTH_SEA;i+=SwingConfig.WIDTH_DECK){
                g2d.drawLine(i,0,i,SwingConfig.HEIGHT_SEA);
            }

    }
}
