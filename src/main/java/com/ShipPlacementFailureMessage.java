package com;

import com.core.SetShipOutcome;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShipPlacementFailureMessage {
    private final SetShipOutcome setShipOutcome;
    public ShipPlacementFailureMessage(SetShipOutcome setShipOutcome) {
        this.setShipOutcome = setShipOutcome;
        String textError;
        switch (setShipOutcome) {
            case SetShipOutcome.SHIP_COLLISION:
                textError = "Ship collision";
                break;
            case SetShipOutcome.SHIP_IS_AVAILABLE:
                textError = "Ship is currently available";
                break;
            case SetShipOutcome.BEYOND_FIELD:
                textError = "The ship collided with the edge of the field";
                break;
            default:
                textError = "Unknown error";
                break;
        }
        JOptionPane optionPane = new JOptionPane(
                textError,
                JOptionPane.ERROR_MESSAGE
        );

        final JDialog dialog = optionPane.createDialog("Ship Placement Error");
        Timer timer = new Timer(1000,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                dialog.setVisible(false);//убрать видимость
                dialog.dispose();//закрыть окно
            }
        });
    timer.setRepeats(false);//таймер не должен перезапускаться
    timer.start();
    dialog.setVisible(true);//окно будет открыто пока таймер не пройдет
    }
}
