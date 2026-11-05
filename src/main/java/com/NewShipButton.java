package com;

import com.core.Orientation;
import com.core.Ship;
import com.displayModel.RemainingShipModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.ColorStartGame.*;

public class NewShipButton extends JButton {
    //private final Container container;
    private final SwingStartGameSea sea;
    private final RemainingShipModel shipModel;
    private Orientation orientation = Orientation.HORIZONTAL;
    private Ship ship;
    public NewShipButton(Ship ship, SwingStartGameSea sea, RemainingShipModel shipModel) {
        this.ship = ship;
        this.sea = sea;
        this.shipModel = shipModel;
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH_DECK*ship.getSize(), SwingConfig.HEIGHT_DECK);
        setBorderPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){

            }
            @Override
            public void mouseReleased(MouseEvent e){

            }

        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){

            }
        });

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
    private void repaintRevalidate(){

    }
    private void drawDeck(Graphics g, Color color){

    }
}
