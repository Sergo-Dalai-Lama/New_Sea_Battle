package com;

import com.core.*;

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
    private Orientation orientation = Orientation.HORIZONTAL;
    private Point offset;
    private Point initialPosition;
    private Coordinate newCoordinate = null;
    private Orientation newOrientation = null;
    private  final int maxDistance = SwingConfig.WIDTH_DECK;
    public ShipButton( Ship ship,SwingStartGameFrame frame,SwingStartGameSea targetPanel) {
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
            public void mousePressed(MouseEvent e){//если зажали на кнопку, то что делать
                if (e.getButton() == MouseEvent.BUTTON3){
                    rotate();
                }
                    mousePressedStart();
                    offset = e.getPoint();//запомнили точку, где нажали на кнопку
                    sourcePanel.remove(ShipButton.this);//удалили кнопку с панели размещения
                    contentPane.add(ShipButton.this, 0);//добавили кнопку на contentPane
                    setLocation(initialPosition);
                    repaintRevalidate();
            }

            @Override
            public void mouseReleased(MouseEvent e){//что делать если кнопку отпустили
                Point position = getLocation();
                //проверяем положение кнопки относительно contentPane
                if (targetPanel.getBounds().contains(position)) { //проверяем опустили ли кнопку над targetPanel
                    Point targetPosition = SwingUtilities.convertPoint(//переводим в систему координат targetPanel
                            contentPane,
                            position,
                            targetPanel
                    );

                     Point point = findNearestSnapPoint(targetPosition);
                     if (point != null) {
                         targetPanel.generatePreCanSetResultTrue();
                         Coordinate coordinate = new Coordinate(point.x/SwingConfig.WIDTH_DECK, point.y/SwingConfig.HEIGHT_DECK);
                         SetShipResult result = null;

                         if (result.outcome()==SetShipOutcome.SET||result.outcome()==SetShipOutcome.SET_FINISHED) {
                             contentPane.remove(ShipButton.this);
                             targetPanel.add(ShipButton.this,0);
                             setLocation(point);
                         } else returnShip();
                     } else returnShip();

                } else returnShip();
                repaintRevalidate();
            }
        }
        );
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){//если перетаскиваем кнопку, то что делать

                mouseDraggedStart(e);
                mouseDraggedAboveSea();

                Container parent = getParent();
                Point position = SwingUtilities.convertPoint(
                       parent,
                       getLocation(),
                       contentPane
                );
                if (resolveMouseCursorPosition(position)!=MouseCursorPosition.SEA_PANEL){
                    if (getLocation().x>getParent().getSize().width||getLocation().y>getParent().getSize().height){
                        returnShip();
                    }
                }
            }
        });
    }
    private void mouseDraggedAboveSea(){
        Point targetPosition;
        if (resolveMouseCursorPosition(getLocation())==MouseCursorPosition.SEA_PANEL){
            targetPosition = repositionMousePosition(MouseCursorPosition.SEA_PANEL,contentPane);
            Point point = findNearestSnapPoint(targetPosition);
        if (point != null) {
            Coordinate coordinate = new Coordinate(point.x/SwingConfig.WIDTH_DECK, point.y/SwingConfig.HEIGHT_DECK);
            if (newCoordinate==null||
                    coordinate.x() != newCoordinate.x() ||
                    coordinate.y() != newCoordinate.y() ||
                    orientation != newOrientation
            ) {
                targetPanel.generatePreCanSetResultTrue();
                newCoordinate = coordinate;
                newOrientation = orientation;
                targetPanel.paintByResult(coordinate,orientation,ship);
            }
        }
        }
    }
private void mouseDraggedStart(MouseEvent e){
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

    private Point findNearestSnapPoint(Point point) {
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

    private void returnShip() {
        if (contentPane!=targetPanel){
            orientation = Orientation.HORIZONTAL;
            setSize(SwingConfig.WIDTH_DECK*ship.getSize(), SwingConfig.HEIGHT_DECK);
        }

        Point sourcePosition = SwingUtilities.convertPoint(//перевести координаты в sourcePanel
                contentPane,
                initialPosition,
                sourcePanel
        );
        contentPane.remove(ShipButton.this);
        sourcePanel.add(ShipButton.this,0);
        setLocation(sourcePosition);
        repaintRevalidate();
    }

    private void rotate() {
        newCoordinate = null;
        if (orientation == Orientation.HORIZONTAL) {
            setSize( SwingConfig.WIDTH_DECK, SwingConfig.HEIGHT_DECK*ship.getSize());
            orientation = Orientation.VERTICAL;
        } else {
            setSize( SwingConfig.WIDTH_DECK * ship.getSize(), SwingConfig.HEIGHT_DECK);
            orientation = Orientation.HORIZONTAL;
        }
        repaint();
    }
    private void repaintRevalidate(){
        sourcePanel.revalidate();
        sourcePanel.repaint();
        contentPane.revalidate();
        contentPane.repaint();
        targetPanel.revalidate();
        targetPanel.repaint();
    }

    private void mousePressedStart(){
        sourcePanel = getParent();//запомнили стартовую панель
        initialPosition = SwingUtilities.convertPoint(
                sourcePanel,
                getLocation(),
                contentPane
        );//перевели координаты в contentPane
    }
    private MouseCursorPosition resolveMouseCursorPosition(Point initialPosition) {
        if (targetPanel.getBounds().contains(initialPosition)) {
            return MouseCursorPosition.SEA_PANEL;
        }
        return MouseCursorPosition.CONTENT_PANE;
    }

    private Point repositionMousePosition(MouseCursorPosition mouseCursorPosition, Container currentContainer) {
       Point newInitialPosition;
        if (mouseCursorPosition==MouseCursorPosition.SEA_PANEL) {
            newInitialPosition = SwingUtilities.convertPoint(
                    currentContainer,
                    getLocation(),
                    targetPanel
            );
        } else {
            newInitialPosition = SwingUtilities.convertPoint(
                    currentContainer,
                    getLocation(),
                    contentPane
            );
        }
        initialPosition = getLocation();
        return newInitialPosition;
    }
}
