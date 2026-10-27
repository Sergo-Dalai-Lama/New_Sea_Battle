package com;

import com.core.Ship;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import static com.ColorStartGame.*;

public class ShipButton extends JButton {
    private SwingStartGameSea targetPanel;
    private Container contentPane;
    private final Ship ship;
    private int offseX;
    private int offseY;
    public ShipButton(Ship ship,SwingStartGameFrame frame,SwingStartGameSea targetPanel) {
        this.ship = ship;
        this.contentPane = frame.getContentPane();
        this.targetPanel = targetPanel;
        setBounds(0, 0, SwingConfig.WIDTH_DECK * ship.getSize(), SwingConfig.HEIGHT_DECK);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        addMouseListener(new  MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){//если нажали на кнопку, то что делать
                offseX = e.getX();
                offseY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e){//что делать если кнопку опустили
                //проверяем опустили ли кнопку над targetPanel
                Point position = SwingUtilities.convertPoint(
                        ShipButton.this,
                        e.getPoint(),
                        contentPane);
                if (targetPanel.getBounds().contains(position)) {
                        moveButton();
                }
            }
        }
        );
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){//если перетаскиваем кнопку, то что делать
                int x = getX()+e.getX()-offseX;
                int y = getY()+e.getY()-offseY;
                setLocation(x,y);
            }
        });
    }

    public void moveButton(){
        Container sourcePanel = getParent();
        Point position = SwingUtilities.convertPoint(//получить координаты относительно contentPane
                sourcePanel,
                this.getLocation(),
                targetPanel
        );
        sourcePanel.remove(this);//удалить кнопку с начальной панели
        Point targetPosition = SwingUtilities.convertPoint(//рассчитать координату на новой панели относительно contentPanel
                contentPane,
                position,
                targetPanel
        );

        targetPanel.add(this);//добавить кнопку на новую панель
        this.setLocation(targetPosition);//установить новую координату
        //обновить обе панели
        sourcePanel.revalidate();
        sourcePanel.repaint();
        targetPanel.revalidate();
        targetPanel.repaint();
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
