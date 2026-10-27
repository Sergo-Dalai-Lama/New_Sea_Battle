package com;

import com.core.Ship;
import javax.swing.*;
import java.awt.*;

import static com.ColorStartGame.*;

public class ShipButton extends JButton {

    public final Ship ship;

    public ShipButton(Ship ship) {
        this.ship = ship;
        setBounds(0, 0, SwingConfig.WIDTH_DECK * ship.getSize(), SwingConfig.HEIGHT_DECK);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }

    public Ship getShip() {
        return ship;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        Color currentColor = SHIP_DECK_OF_NON_CURSOR;
        if (getModel().isPressed()) {
            currentColor = SHIP_DECK_OF_CURSOR;
            g2d.translate(2,2);
        } else if (getModel().isRollover()) {
            currentColor = SHIP_DECK_OF_PRESSED;
        }
        drawDeck(g2d,currentColor);
        g2d.setColor(getForeground());
        g2d.dispose();
    }

    private void drawDeck(Graphics2D g2d, Color color) {
        int widthShip = getWidth();
        int heightDeck = getHeight();
        int widthDeck = widthShip/ship.getSize();
        for (int i = 0; i <ship.getSize(); i++) {
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(i*widthDeck,0,widthDeck,heightDeck);
            g2d.setColor(color);
            g2d.fillRect(i*widthDeck,0,widthDeck,heightDeck);
        }
    }
}
