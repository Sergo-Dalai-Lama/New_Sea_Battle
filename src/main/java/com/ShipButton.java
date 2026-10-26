package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ShipButton extends JButton {
    Color colorNonCursorOfButton = new Color(202, 255, 153,50);
    Color colorCursorOfButton = new Color(202, 255, 153,100);
    Color colorPressedOnButton = new Color(202, 255, 153,70);
    public final int size;
    private int offseX;
    private int offseY;
    public ShipButton(int size) {
        this.size = size;

        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        addMouseListener(new  MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                offseX = e.getX();
                offseY = e.getY();
            }
        });
        addMouseMotionListener(new   MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                int x = getX()+e.getX()-offseX;
                int y = getY()+e.getY()-offseY;
                setLocation(x,y);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        Color currentColor = colorNonCursorOfButton;
        if (getModel().isPressed()) {
            currentColor = colorPressedOnButton;
            g2d.translate(2,2);
        } else if (getModel().isRollover()) {
            currentColor = colorCursorOfButton;
        }
        drawDeck(g2d,currentColor);
        g2d.setColor(getForeground());
        g2d.dispose();
    }

    private void drawDeck(Graphics2D g2d, Color color) {
        int widthShip = getWidth();
        int heightDeck = getHeight();
        int widthDeck = widthShip/size;
        for (int i = 0; i <size; i++) {
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(i*widthDeck,0,widthDeck,heightDeck);
            g2d.setColor(color);
            g2d.fillRect(i*widthDeck,0,widthDeck,heightDeck);
        }
    }
}
