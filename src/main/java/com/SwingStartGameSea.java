package com;
import com.core.Coordinate;
import com.core.GameConfig;
import com.core.GameRules;
import com.core.ListShip;
import com.ColorStartGame.*;
import com.displayModel.MySeaDisplayModel;

import javax.swing.*;
import java.awt.*;
import static com.ColorStartGame.BORDER_CELL;
import static com.ColorStartGame.FILLED_CELL;

public class SwingStartGameSea extends JPanel {
  //  private final MySeaDisplayModel mySeaDisplayModel;
    public SwingStartGameSea() {
       // this.mySeaDisplayModel = mySeaDisplayModel;
        setBounds(0, 0, SwingConfig.WIDTH_SEA, SwingConfig.HEIGHT_SEA);
    }

    private void paintSeaBattle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(BORDER_CELL);
        g2d.setStroke(new BasicStroke(1));

        for (int i = 0;i<=SwingConfig.HEIGHT_SEA;i+=SwingConfig.HEIGHT_DECK){
            g2d.drawLine(0,i,SwingConfig.WIDTH_SEA,i);
        }
        for (int i = 0;i<=SwingConfig.WIDTH_SEA;i+=SwingConfig.WIDTH_DECK){
            g2d.drawLine(i,0,i,SwingConfig.HEIGHT_SEA);
        }
        g2d.setColor(FILLED_CELL);
        g2d.fillRect(0,0,SwingConfig.WIDTH_SEA,SwingConfig.HEIGHT_SEA);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        paintSeaBattle(g2d);
    }

    private Coordinate convertPanelToSeaCoordinate(int x,int y){
        return new Coordinate(x*SwingConfig.WIDTH_DECK,y*SwingConfig.HEIGHT_DECK);
        //поделит до целого, т.к. x и y это int
    }

    public Coordinate getCoordinate(int x,int y){
        return convertPanelToSeaCoordinate(x,y);
    }
}
