package com;

import com.core.GameConfig;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ColorStartGame.BORDER_CELL;
import static com.ColorStartGame.FILLED_CELL;

public class SwingStartGameSea extends JPanel {
  //  private final MySeaDisplayModel mySeaDisplayModel;
    private final List<Point> points = new ArrayList<Point>();
    public SwingStartGameSea() {
       // this.mySeaDisplayModel = mySeaDisplayModel;
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH_SEA, SwingConfig.HEIGHT_SEA);
        generatePoints();
    }

    private void paintSeaBattle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(FILLED_CELL);
        g2d.fillRect(0,0,SwingConfig.WIDTH_SEA,SwingConfig.HEIGHT_SEA);
        g2d.setColor(BORDER_CELL);
        g2d.setStroke(new BasicStroke(1));

        for (int i = 0;i<=SwingConfig.HEIGHT_SEA;i+=SwingConfig.HEIGHT_DECK){
            g2d.drawLine(0,i,SwingConfig.WIDTH_SEA,i);
        }
        for (int i = 0;i<=SwingConfig.WIDTH_SEA;i+=SwingConfig.WIDTH_DECK){
            g2d.drawLine(i,0,i,SwingConfig.HEIGHT_SEA);
        }
    }

    @Override
protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            paintSeaBattle(g2d);
        } finally {
            g2d.dispose();
        }
    }
    private void generatePoints() {
        for (int i = 0; i< GameConfig.ROWS;i++){
            for (int j = 0; j< GameConfig.COLUMNS;j++){
                Point p = new Point(i*SwingConfig.WIDTH_DECK,j*SwingConfig.HEIGHT_DECK);
                points.add(p);
            }
        }

    }
    public List<Point> getPoints() {
        return List.copyOf(points);
    }
}
