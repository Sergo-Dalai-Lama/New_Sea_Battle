package com;

import com.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.ColorStartGame.*;

public class ShipButton extends JButton {
    public interface MouseShipListener {
        void onPressed(ShipButton shipButton, MouseEvent e);
        void onReleased(ShipButton shipButton, MouseEvent e);
        void onDragged(ShipButton shipButton, MouseEvent e);
        void onRotated(ShipButton shipButton, MouseEvent e);
    }
    private MouseShipListener mouseShipListener;
    private final SwingStartGameFrame frame;
    private Container shipListContainer;
    private Container sourcePanel=null;
    private final Ship ship;
    private Orientation orientation = Orientation.HORIZONTAL;
    private final Container contentPane;
    private Point initialLocation;
    private Point offset;
    private final int maxDistance = SwingConfig.WIDTH_DECK;
    private Coordinate newCoordinate = null;
    private Orientation newOrientation = null;
    private ShipSetting shipSetting;
    public ShipButton(Ship ship, SwingStartGameFrame frame, PlacementShipButton controller) {
        this.ship = ship;
        this.contentPane = frame.getContentPane();
        this.frame = frame;
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH_DECK*ship.getSize(), SwingConfig.HEIGHT_DECK);
        setBorderPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){//метод вызывается когда зажали на компоненте курсор

            if (e.getButton() == MouseEvent.BUTTON3){
                controller.onRotated(ShipButton.this, e);
            }
            sourcePanel=getParent();
            if (sourcePanel!=frame.getSea()){//если объект зажали не на поле боя, то зажали на панели размещения
                shipListContainer=getParent();
                shipSetting = ShipSetting.SET;
            } else {
                shipSetting = ShipSetting.REPOSITION;
                Point nearestPoint = findNearestSnapPoint(getLocation());
                if (nearestPoint!=null) {
                    Coordinate coordinate = new Coordinate(nearestPoint.x/SwingConfig.WIDTH_DECK, nearestPoint.y/SwingConfig.HEIGHT_DECK);
                    applyDeleteShip(coordinate);
                }
            }
            mouseShipListener.onPressed(ShipButton.this, e);
            initialLocation = SwingUtilities.convertPoint(//перевели из координат панели, на которой зажали
                    // и перевели в координаты contentPane
                    sourcePanel,
                    getLocation(),
                    contentPane
            );

            offset = e.getPoint();//запомнили координаты нажатого курсора на объекте
            sourcePanel.remove(ShipButton.this);
            contentPane.add(ShipButton.this, 0);
            setLocation(initialLocation);
            repaintRevalidate();
            }

            @Override
            public void mouseReleased(MouseEvent e) {//вызывается когда мышь отпустили
             /*   SetShipResult result = frame.getSea().setShipResult(newCoordinate, ship, orientation);
                frame.getRemaining().applySet(result);
                SetShipOutcome setShipOutcome = result.outcome();
                if (setShipOutcome == SetShipOutcome.SET||setShipOutcome==SetShipOutcome.SET_FINISHED){
                    setShip();
                } else {
                    setError(setShipOutcome);
                }*/

                controller.onReleased(ShipButton.this, e);
                repaintRevalidate();
            }

        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){//вызывается когда курсор перемещается с зажатой клавишей на компоненте

                Point position = SwingUtilities.convertPoint(//перевели в систему координат contentPane
                        ShipButton.this,
                        e.getPoint(),
                        contentPane
                );
                //реализация настроек курсора
                setupCursor(position);
                //реализация превью установки
                applyPlacement();
                repaintRevalidate();
            }
        });
    }

    private void setupCursor(Point position) {
        //запомнили куда сместилась мышь на экране
        int thisX = position.x;
        int thisY = position.y;
        //вычислили координаты нового положения курсора после будущего смещения
        int nextX = thisX - offset.x;
        int nextY = thisY - offset.y;
        //границы поля перетаскивания
        int minX = 0;
        int minY = 0;
        int maxX = SwingConfig.WIDTH- ShipButton.this.getWidth();
        int maxY = SwingConfig.HEIGHT- ShipButton.this.getHeight();

        //удерживаем nextX в пределах minX и maxX
        nextX = Math.clamp(nextX, minX, maxX);
        nextY = Math.clamp(nextY, minY, maxY);
        setLocation(nextX, nextY);//применяем новые координаты курсора
    }

    private void applyPlacement() {
        if (isShipOverSea(getLocation())) {
            Point nearestPoint = findNearestSnapPoint(getLocation());
            if (nearestPoint != null) {
                Coordinate coordinate = new Coordinate(nearestPoint.x/SwingConfig.WIDTH_DECK, nearestPoint.y/SwingConfig.HEIGHT_DECK);
                if (!coordinate.equals(newCoordinate)||
                    !orientation.equals(newOrientation)) {
                    applyPrePlacementPaint(coordinate, frame.getSea(), ship);
                }
            }
        } else  {
            resetPaint();
        }
    }

    private void applyPrePlacementPaint(Coordinate coordinate, SwingStartGameSea sea, Ship ship) {
        sea.generatePreCanSetResultTrue();
        newCoordinate = coordinate;
        newOrientation = orientation;
        sea.paintByResult(coordinate,orientation, ship);
    }

    private void applyDeleteShip(Coordinate coordinate) {
        frame.getSea().generatePreCanSetResultTrue();
        frame.getSea().unSetShip(coordinate);
    }

    private void resetPaint(){
        frame.getSea().generatePreCanSetResultTrue();
        newCoordinate = null;
        newOrientation = null;
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
    public void repaintRevalidate(){
        contentPane.revalidate();
        contentPane.repaint();

        frame.getSea().revalidate();
        frame.getSea().repaint();

        shipListContainer.revalidate();
        shipListContainer.repaint();
    }/*
    private void returnShipIsList(){//метод возвращения корабля в список
        orientation = Orientation.HORIZONTAL;
        setSize(SwingConfig.WIDTH_DECK*ship.getSize(),SwingConfig.HEIGHT_DECK);
        Point sourcePosition = SwingUtilities.convertPoint(//перевести координаты в sourcePanel
                contentPane,
                initialLocation,
                shipListContainer
        );
        frame.getSea().generatePreCanSetResultTrue();
        contentPane.remove(ShipButton.this);
        shipListContainer.add(ShipButton.this, 0);
        setLocation(sourcePosition);
        repaintRevalidate();
    }*/

    private void returnShipIsSea(){
            Point sourcePosition = SwingUtilities.convertPoint(
                    contentPane,
                    initialLocation,
                    frame.getSea()
            );
        frame.getSea().generatePreCanSetResultTrue();
            contentPane.remove(ShipButton.this);
        frame.getSea().add(ShipButton.this,0);
            setLocation(sourcePosition);
            repaintRevalidate();
    }

    private boolean isShipOverSea(Point posInContentPane){//метод проверки нахождения над Sea

        return frame.getSea().getBounds().contains(posInContentPane);
    }

    private Point findNearestSnapPoint(Point point) {

        if (getParent()!=frame.getSea()){
            point = SwingUtilities.convertPoint(
                    getParent(),
                    point,
                    frame.getSea()
            );
        }
        Point nearestPoint = null;
        double minDistance = Double.MAX_VALUE;
        for (Point snapPoint: frame.getSea().getPoints()){
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
    /*
        private void setShip() {
            Point nearestPoint = findNearestSnapPoint(getLocation());
            if (nearestPoint!=null) {
                contentPane.remove(ShipButton.this);
                frame.getSea().add(ShipButton.this,0);
                setLocation(nearestPoint);
            }
        }

    private void setError(SetShipOutcome setShipOutcome) {
        if (shipSetting==ShipSetting.SET) {
            returnShipIsList();
        }
        if (shipSetting==ShipSetting.REPOSITION) {
            returnShipIsSea();
        }
        new ShipPlacementFailureMessage(setShipOutcome);
    }  */

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
