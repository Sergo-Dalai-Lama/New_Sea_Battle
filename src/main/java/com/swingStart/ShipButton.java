package com.swingStart;

import com.SwingConfig;
import com.core.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static com.swingStart.ColorStartGame.*;

public class ShipButton extends JButton {
    public interface MouseShipListener {
        void onPressed(ShipButton shipButton, MouseEvent e);
        void onReleased(ShipButton shipButton, MouseEvent e);
        void onDragged(ShipButton shipButton, MouseEvent e);
        void onRotated(ShipButton shipButton, MouseEvent e);
    }
    private MouseShipListener mouseShipListener;
    private final Ship ship;
    private Orientation orientation = Orientation.HORIZONTAL;
    private Coordinate newCoordinate = null;
    private Orientation newOrientation = null;

    public ShipButton(Ship ship) {
        this.ship = ship;
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH_DECK*ship.getSize(), SwingConfig.HEIGHT_DECK);
        setBorderPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){//метод вызывается когда зажали на компоненте курсор

            if (e.getButton() == MouseEvent.BUTTON3){
            mouseShipListener.onRotated(ShipButton.this, e);
            }
            mouseShipListener.onPressed(ShipButton.this, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {//вызывается когда мышь отпустили
            mouseShipListener.onReleased(ShipButton.this, e);
            }

        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){//вызывается когда курсор перемещается с зажатой клавишей на компоненте
            mouseShipListener.onDragged(ShipButton.this, e);
            }
        });
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
    private void drawDeck(Graphics2D g2d, Color color){
            if (orientation == Orientation.HORIZONTAL) {
                for (int i = 0; i <ship.getSize(); i++) {
                    g2d.setColor(color);
                    g2d.fillRect(i*SwingConfig.WIDTH_DECK,0,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);
                    g2d.setColor(BORDER_CELL);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRect(i*SwingConfig.WIDTH_DECK,0,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);

                }
            } else  {
                for (int i = 0; i <ship.getSize(); i++) {
                    g2d.setColor(color);
                    g2d.fillRect(0,i*SwingConfig.HEIGHT_DECK,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);
                    g2d.setColor(BORDER_CELL);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRect(0,i*SwingConfig.HEIGHT_DECK,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);
                }
            }
    }

    public void rotateShip(){
        newOrientation = null;
        if (orientation != Orientation.VERTICAL) {
            orientation = Orientation.VERTICAL;
            setSize(SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK*ship.getSize());
        } else {
            orientation = Orientation.HORIZONTAL;
            setSize(SwingConfig.WIDTH_DECK*ship.getSize(),SwingConfig.HEIGHT_DECK);
        }
        repaint();
    }

    public void setMouseListener(MouseShipListener mouseShipListener) {
        this.mouseShipListener = mouseShipListener;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
    public Orientation getOrientation() {
        return orientation;
    }

    public int getShipSize(){
        return ship.getSize();
    }

    public  Ship getShip(){
        return ship;
    }
}
