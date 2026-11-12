package com;

import com.core.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

//так выглядит стандартный контроллер взаимодействия между моделью/ядром и вью
public class ControllerPlacement implements ShipButton.MouseShipListener {
    private final SwingStartGameSea sea;
    private final GameRules gameRules;
    private final Container contentPane;
    private final SwingStartGameRemaining remaining;
    private final GenerateSetShipButton generateSetShipButton;
    private Orientation newOrientation = null;
    private Coordinate newCoordinate = null;
    private final int maxDistance = SwingConfig.WIDTH_DECK;
    private Point initialPosition;
    private Point offset;
    private Coordinate coordinateForReposition = null;
    private ShipSetting shipSetting;
    private StartBattleButton startBattleButton;
    public ControllerPlacement(SwingStartGameSea sea, SwingStartGameRemaining remaining, StartBattleButton startBattleButton,GenerateSetShipButton generateSetShipButton, Container contentPane, GameRules gameRules) {
        this.sea = sea;
        this.gameRules = gameRules;
        this.contentPane = contentPane;
        this.remaining = remaining;
        this.startBattleButton = startBattleButton;
        this.generateSetShipButton = generateSetShipButton;
    generateSetShipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameRules.generationShipFormation();//сгенерировали расстановку
                remaining.removeAll();//удалили корабли из списка
                //расставили корабли на поле
                sea.removeAll();

                for (ShipForPlacementState shipForPlacementState: gameRules.getShipsForPlacement()){
                    Ship ship = shipForPlacementState.ship;
                    Orientation orientation = shipForPlacementState.orientation;
                    Coordinate coordinate = shipForPlacementState.coordinate;
                    ShipButton shipButton = new ShipButton(ship);
                    shipButton.setMouseListener(ControllerPlacement.this);
                    shipButton.setOrientation(orientation);
                    if (orientation == Orientation.VERTICAL) {
                        shipButton.setSize(SwingConfig.WIDTH_DECK, SwingConfig.HEIGHT_DECK*shipButton.getShipSize());
                    }
                    sea.add(shipButton,0);
                    shipButton.setLocation(coordinateToPoint(coordinate));
                }
                //перерисовали всё
                repaintRevalidate();
                startBattleButton.setEnabledButton();
            }
        });
    }
    @Override
    public void onPressed(ShipButton shipButton, MouseEvent e){

        if (isPressedOverSea(shipButton.getParent())) {
            startBattleButton.setDisabledButton();
            shipSetting=ShipSetting.REPOSITION;
            initialPosition = SwingUtilities.convertPoint(
                    sea,
                    shipButton.getLocation(),
                    contentPane
            );
            Point nearestPoint = findNearestSnapPoint(shipButton,initialPosition);
            if (nearestPoint!=null) {
                coordinateForReposition = pointToCoordinate(nearestPoint);
                UnSetShipCommand command = new UnSetShipCommand(coordinateForReposition);
                gameRules.unSetShip(command);
                sea.generatePreCanSetResultTrue();
            }
            sea.remove(shipButton);
            contentPane.add(shipButton, 0);
        } else {
            shipSetting=ShipSetting.SET;
            initialPosition = SwingUtilities.convertPoint(
                    remaining,
                    shipButton.getLocation(),
                    contentPane
            );
            remaining.remove(shipButton);
            contentPane.add(shipButton, 0);
        }
        offset = e.getPoint();
        shipButton.setLocation(initialPosition);
        repaintRevalidate();

    }
    @Override
    public void onReleased(ShipButton shipButton, MouseEvent e){
        if (isShipOverSea(shipButton.getLocation())){//сначала засекли, что отпустили кнопку над sea
            Point position = SwingUtilities.convertPoint(//перевели координаты в sea
                    contentPane,
                    shipButton.getLocation(),
                    sea
            );
            Point nearestPoint = findNearestSnapPoint(shipButton,position);//вычислили ближайшую точку привязки
            if (nearestPoint != null){
                Coordinate coordinate = pointToCoordinate(nearestPoint);//перевели точку привязки в координату
                    //дать команду на попытку установить в remaining, sea, contentPane
                    SetShipResult result = gameRules.setShip(new SetShipCommand(coordinate,shipButton.getShip(),shipButton.getOrientation()));
                    if (result.outcome() == SetShipOutcome.SET||result.outcome()==SetShipOutcome.SET_FINISHED) {
                        remaining.applySet(result);
                        contentPane.remove(shipButton);
                        sea.add(shipButton, 0);
                        shipButton.setLocation(nearestPoint);
                        if (result.outcome() == SetShipOutcome.SET_FINISHED) {
                            startBattleButton.setEnabledButton();
                        }
                    } else {
                        if (shipSetting == ShipSetting.SET) {
                            returnShipIsList(shipButton);
                            setError(result.outcome(), shipButton);
                        }
                        if (shipSetting == ShipSetting.REPOSITION) {
                            returnShipIsSea(shipButton);
                            setError(result.outcome(), shipButton);
                        }
                    }
            }
        } else {
            if (shipSetting == ShipSetting.SET) {
                returnShipIsList(shipButton);
            }
            if (shipSetting == ShipSetting.REPOSITION) {
                returnShipIsSea(shipButton);
            }
            new ShipPlacementFailureMessage(SetShipOutcome.INVALID_DATA);

        }
        sea.generatePreCanSetResultTrue();
        repaintRevalidate();
    }
    @Override
    public void onDragged(ShipButton shipButton, MouseEvent e){
        Point position = SwingUtilities.convertPoint(
                shipButton,
                e.getPoint(),
                contentPane
        );
        setupCursor(position,shipButton);
        applyPlacement(shipButton);
        repaintRevalidate();
    }
    @Override
    public void onRotated(ShipButton shipButton, MouseEvent e){
        shipButton.rotateShip();
    }

    private boolean isShipOverSea(Point pointIsContentPane){
        return sea.getBounds().contains(pointIsContentPane);
    }

    private boolean isPressedOverSea(Container container){
        return sea==container;
    }

    private Coordinate pointToCoordinate(Point point){
         return new Coordinate(point.x/SwingConfig.WIDTH_DECK, point.y/SwingConfig.HEIGHT_DECK);
    }
    private Point coordinateToPoint(Coordinate coordinate){
        return new Point(coordinate.x()*SwingConfig.WIDTH_DECK, coordinate.y()*SwingConfig.HEIGHT_DECK);
    }

    private Point findNearestSnapPoint(ShipButton shipButton,Point point) {

         point = SwingUtilities.convertPoint(
                shipButton.getParent(),
                shipButton.getLocation(),
                sea
        );

        Point nearestPoint = null;
        double minDistance = Double.MAX_VALUE;
        for (Point snapPoint: sea.getPoints()){
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
    private void returnShipIsList(ShipButton shipButton){//метод возвращения корабля в список
        shipButton.setOrientation(Orientation.HORIZONTAL);
        shipButton.setSize(SwingConfig.WIDTH_DECK*shipButton.getShipSize(),SwingConfig.HEIGHT_DECK);
        Point sourcePosition = SwingUtilities.convertPoint(//перевести координаты в remaining
                contentPane,
                initialPosition,
                remaining
        );
        sea.generatePreCanSetResultTrue();
        contentPane.remove(shipButton);
        remaining.add(shipButton, 0);
        shipButton.setLocation(sourcePosition);
        repaintRevalidate();
    }
    private void returnShipIsSea(ShipButton shipButton){
        Point sourcePosition = SwingUtilities.convertPoint(
                contentPane,
                initialPosition,
                sea
        );
        sea.generatePreCanSetResultTrue();
        contentPane.remove(shipButton);
        sea.add(shipButton,0);
        shipButton.setLocation(sourcePosition);
        repaintRevalidate();
    }

    private void setError(SetShipOutcome setShipOutcome,ShipButton shipButton) {
        if (shipSetting == ShipSetting.SET) {
            returnShipIsList(shipButton);
        }
        if (shipSetting == ShipSetting.REPOSITION) {
            returnShipIsSea(shipButton);
        }
        new ShipPlacementFailureMessage(setShipOutcome);
    }
    private void repaintRevalidate(){
        contentPane.revalidate();
        contentPane.repaint();

        sea.revalidate();
        sea.repaint();

        remaining.revalidate();
        remaining.repaint();
    }

    private void resetPaint(){
        sea.generatePreCanSetResultTrue();
        newCoordinate = null;
        newOrientation = null;
    }

    private void setupCursor(Point position,ShipButton shipButton) {
        //запомнили куда сместилась мышь на экране
        int thisX = position.x;
        int thisY = position.y;
        //вычислили координаты нового положения курсора после будущего смещения
        int nextX = thisX - offset.x;
        int nextY = thisY - offset.y;
        //границы поля перетаскивания
        int minX = 0;
        int minY = 0;
        int maxX = SwingConfig.WIDTH- shipButton.getWidth();
        int maxY = SwingConfig.HEIGHT- shipButton.getHeight();

        //удерживаем nextX в пределах minX и maxX
        nextX = Math.clamp(nextX, minX, maxX);
        nextY = Math.clamp(nextY, minY, maxY);
        shipButton.setLocation(nextX, nextY);//применяем новые координаты курсора
    }
    private void applyPlacement(ShipButton shipButton) {
        if (isShipOverSea(shipButton.getLocation())) {
            Point nearestPoint = findNearestSnapPoint(shipButton,shipButton.getLocation());
            if (nearestPoint != null) {
                Coordinate coordinate = pointToCoordinate(nearestPoint);
                if (!coordinate.equals(newCoordinate)||
                     shipButton.getOrientation()!=newOrientation) {
                        applyPrePlacementPaint(coordinate,shipButton);
                }
            }
        } else {
            resetPaint();
        }
    }

    private void applyPrePlacementPaint(Coordinate coordinate, ShipButton shipButton) {
        sea.generatePreCanSetResultTrue();
        newCoordinate = coordinate;
        newOrientation = shipButton.getOrientation();
        sea.paintByResult(coordinate, shipButton.getOrientation(), shipButton);
    }

}
