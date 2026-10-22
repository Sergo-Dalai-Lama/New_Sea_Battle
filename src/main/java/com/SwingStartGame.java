package com;

import com.core.GameConfig;
import com.core.GameRules;
import com.core.ListShip;
import com.core.Ship;
import com.displayModel.RemainingShipModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SwingStartGame extends JPanel {
    ListShip listShip =  new ListShip();
    GameRules gameRules = new GameRules(listShip);
    RemainingShipModel remainingShipModel = new RemainingShipModel();
    JFrame frame =  new JFrame("New Sea Battle");

    public SwingStartGame() {
        setPreferredSize(new Dimension(SwingConfig.WIDTH, SwingConfig.HEIGHT ));
        setBackground(Color.gray);
        setLayout(null);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public  void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSea(g);
        paintListShips(g);
    }

    private void paintSea(Graphics g) {
        int x = (SwingConfig.WIDTH-SwingConfig.WIDTH_SEA)/4;
        int y = (SwingConfig.HEIGHT-SwingConfig.HEIGHT_SEA)/2;
        int width = x+SwingConfig.WIDTH_SEA;
        int height = y+SwingConfig.HEIGHT_SEA;
        Graphics2D frameSea = (Graphics2D) g;
        frameSea.setColor(Color.black);
        frameSea.setStroke(new BasicStroke(5));
        frameSea.drawRect(x, y, SwingConfig.WIDTH_SEA, SwingConfig.HEIGHT_SEA);
        frameSea.setColor(new Color(135, 206, 250, 100));
        frameSea.fillRect(x, y, SwingConfig.WIDTH_SEA, SwingConfig.HEIGHT_SEA);
        Graphics2D gridSea = (Graphics2D) g;
        gridSea.setColor(Color.black);
        gridSea.setStroke(new BasicStroke(1));
        for (int i = 0;i<SwingConfig.HEIGHT_SEA;i+=SwingConfig.HEIGHT_SEA/ GameConfig.ROWS){
            gridSea.drawLine(x,y+i,width,y+i);
        }
        for (int i = 0;i<SwingConfig.WIDTH_SEA;i+=SwingConfig.WIDTH_SEA/ GameConfig.COLUMNS){
            gridSea.drawLine(x+i,y,x+i,height);
        }
    }

    private void paintListShips(Graphics g) {
        int widthDeck = SwingConfig.WIDTH_SEA/ GameConfig.COLUMNS;//ширина
        int heightDeck = SwingConfig.HEIGHT_SEA/ GameConfig.ROWS;//высота

        int y = SwingConfig.HEIGHT/4;
        Graphics2D frameDeck = (Graphics2D) g;

        List<Ship[]> mySeaRemainingSetShipModel = remainingShipModel.getMySeaRemainingSetShipModel();

        for (int i =0; i< mySeaRemainingSetShipModel.size();i++) {//цикл по типам кораблей
            int x = (int) (0.65*SwingConfig.WIDTH);
            for (int j = 0; j < mySeaRemainingSetShipModel.get(i).length; j++) {//цикл по кораблям типа
                for (int k = 0; k < 4-i; k++) {//цикл по бортам одного корабля
                    if (mySeaRemainingSetShipModel.get(i)[j] == null) {
                        frameDeck.setColor(Color.black);
                        frameDeck.setStroke(new BasicStroke(1));
                        frameDeck.drawRect(x+k*widthDeck,y+i*heightDeck,widthDeck,heightDeck);
                    } else {
                        frameDeck.setColor(Color.black);
                        frameDeck.setStroke(new BasicStroke(1));
                        frameDeck.drawRect(x+k*widthDeck,y+i*heightDeck,widthDeck,heightDeck);
                        frameDeck.setColor(new Color(202, 255, 153,50));
                        frameDeck.fillRect(x+k*widthDeck,y+i*heightDeck,widthDeck,heightDeck);
                    }
                }
                x+= (int)(1.2*mySeaRemainingSetShipModel.get(i)[0].getSize()*widthDeck);
            }
            y+=heightDeck/2;
        }

    }
}
