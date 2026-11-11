package com;

import com.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

//так выглядит стандартный контроллер взаимодействия между моделью/ядром и вью
public class PlacementShipButton implements ShipButton.MouseShipListener {
    private final SwingStartGameSea sea;
    private final GameRules gameRules;
    private final Container contentPane;
    private final SwingStartGameRemaining remaining;
    private Orientation newOrientation = null;
    private Coordinate newCoordinate = null;
    private final int maxDistance = SwingConfig.WIDTH_DECK;
    private Point initialPosition;
    private Point offset;
    public PlacementShipButton(SwingStartGameSea sea, SwingStartGameRemaining remaining, Container contentPane, GameRules gameRules) {
        this.sea = sea;
        this.gameRules = gameRules;
        this.contentPane = contentPane;
        this.remaining = remaining;

    }
    @Override
    public void onPressed(ShipButton shipButton, MouseEvent e){
        initialPosition = SwingUtilities.convertPoint(//перевели из координат панели, на которой зажали
                // и перевели в координаты contentPane
                remaining,
                shipButton.getLocation(),
                contentPane
        );
    }
    @Override
    public void onReleased(ShipButton shipButton, MouseEvent e){
        if (isShipOverSea(shipButton.getLocation())){//сначала засекли, что отпустили кнопку над sea
            Point position = SwingUtilities.convertPoint(//перевели координаты в sea
                    contentPane,
                    shipButton.getLocation(),
                    sea
            );
            Point nearestPoint = findNearestSnapPoint(position);//вычислили ближайшую точку привязки
            if (nearestPoint != null){
                Coordinate coordinate = positionToCoordinate(nearestPoint);//перевели точку привязки в координату
                if (!coordinate.equals(newCoordinate)||
                !shipButton.getOrientation().equals(newOrientation)){
                    //дать команду на попытку установить в remaining, sea, contentPane
                    SetShipCommand command = new SetShipCommand(coordinate,shipButton.getShip(),shipButton.getOrientation());//создали команду
                    SetShipResult result = gameRules.setShip(command);//отправили команду в ядро
                    if (result.outcome() == SetShipOutcome.SET||result.outcome()==SetShipOutcome.SET_FINISHED) {

                        remaining.applySet(result);
                        contentPane.remove(shipButton);
                        sea.add(shipButton, 0);
                        shipButton.setLocation(nearestPoint);
                    } else {
                        returnShipIsList(shipButton);
                        new ShipPlacementFailureMessage(result.outcome());
                    }
                }
            }
        } else {
            returnShipIsList(shipButton);
            new ShipPlacementFailureMessage(SetShipOutcome.INVALID_DATA);

        }

    }
    @Override
    public void onDragged(ShipButton shipButton, MouseEvent e){

    }
    @Override
    public void onRotated(ShipButton shipButton, MouseEvent e){
        shipButton.rotateShip();
    }

    private boolean isShipOverSea(Point pointIsContentPane){
        return sea.getBounds().contains(pointIsContentPane);
    }

    private Coordinate positionToCoordinate(Point point){
         return new Coordinate(point.x/SwingConfig.WIDTH_DECK, point.y/SwingConfig.HEIGHT_DECK);
    }

    private Point findNearestSnapPoint(Point point) {

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
        contentPane.remove(shipButton);
        remaining.add(shipButton, 0);
        shipButton.setLocation(sourcePosition);
        sea.generatePreCanSetResultTrue();
        shipButton.repaintRevalidate();
    }

}
