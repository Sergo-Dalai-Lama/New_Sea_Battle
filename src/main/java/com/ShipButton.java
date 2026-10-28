package com;

import com.core.Ship;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import static com.ColorStartGame.*;

public class ShipButton extends JButton {
    private final SwingStartGameSea targetPanel;
    private final Container contentPane;
    private Container sourcePanel;//для сохранения информации об исходной панели
    private final Ship ship;
    private Point offset;
    private Point initialPosition;

    public ShipButton(Ship ship,SwingStartGameFrame frame,SwingStartGameSea targetPanel) {
        this.ship = ship;
        this.contentPane = frame.getContentPane();
        this.targetPanel = targetPanel;
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH_DECK * ship.getSize(), SwingConfig.HEIGHT_DECK);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        addMouseListener(new  MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){//если нажали на кнопку, то что делать
                sourcePanel = getParent();//запоминаем исходную панель

                initialPosition = SwingUtilities.convertPoint(//перевели координаты в contentPane
                        sourcePanel,
                        getLocation(),
                        contentPane);

                offset = e.getPoint();//запомнили точку. где нажали на кнопку
                sourcePanel.remove(ShipButton.this);//удалили кнопку с панели размещения
                contentPane.add(ShipButton.this,0);//добавили кнопку на contentPane
                setLocation(initialPosition);
                sourcePanel.revalidate();
                sourcePanel.repaint();

                contentPane.revalidate();
                contentPane.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e){//что делать если кнопку опустили
                Point position = getLocation();
                //проверяем положение кнопки относительно contentPane
                if (targetPanel.getBounds().contains(position)) { //проверяем опустили ли кнопку над targetPanel
                    Point targetPosition = SwingUtilities.convertPoint(//переводим в систему координат targetPanel
                            contentPane,
                            position,
                            targetPanel
                    );

                     Point point = findNearestSnapPoint(targetPosition,SwingConfig.WIDTH_DECK);
                     if (point != null) {
                         contentPane.remove(ShipButton.this);
                         targetPanel.add(ShipButton.this,0);
                         setLocation(point);
                     }

                } else {
                    Point sourcePosition = SwingUtilities.convertPoint(//перевести координаты в sourcePanel
                         contentPane,
                         initialPosition,
                         sourcePanel
                    );
                    contentPane.remove(ShipButton.this);
                    sourcePanel.add(ShipButton.this,0);
                    setLocation(sourcePosition);
                }

                contentPane.revalidate();
                contentPane.repaint();

                sourcePanel.revalidate();
                sourcePanel.repaint();

                targetPanel.revalidate();
                targetPanel.repaint();
            }
        }
        );
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){//если перетаскиваем кнопку, то что делать
                Point mousePosition = SwingUtilities.convertPoint(//переводим координаты курсора в contentPane
                        ShipButton.this,
                        e.getPoint(),
                        contentPane
                );
                //перехватываем кнопку с сохранением точки захвата
                int x = mousePosition.x - offset.x;
                int y = mousePosition.y - offset.y;
                setLocation(x, y);
            }
        });
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
            g2d.setColor(color);
            g2d.fillRect(i*widthDeck,0,widthDeck,heightDeck);
            g2d.setColor(BORDER_CELL);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(i*widthDeck,0,widthDeck,heightDeck);

        }
    }

    private Point findNearestSnapPoint(Point point, int maxDistance) {
        Point nearestPoint = null;
        double minDistance = Double.MAX_VALUE;
        for (Point snapPoint: targetPanel.getPoints()){
            double distance = point.distance(snapPoint);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = snapPoint;
            }
        }
        if (nearestPoint!=null&&minDistance <= maxDistance) {
            return nearestPoint;
        }

        return null;
    }
}
