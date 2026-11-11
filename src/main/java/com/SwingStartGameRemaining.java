package com;

import com.core.SetShipResult;
import com.core.UnSetShipResult;
import com.displayModel.RemainingShipModel;
import javax.swing.*;
import java.awt.event.MouseEvent;

public class SwingStartGameRemaining extends JPanel {
    private final RemainingShipModel remainingShipModel = new RemainingShipModel();
    private final SwingStartGameFrame frame;
    private PlacementShipButton controller;
    public SwingStartGameRemaining(SwingStartGameFrame frame) {
        this.frame = frame;
        setLayout(null);
        setOpaque(false);
        setBounds(0,
                0,
                SwingConfig.WIDTH-SwingConfig.WIDTH_SEA,
                SwingConfig.HEIGHT_SEA);

    }

    private void setRemainingShip() {
        for (int i = 0; i<remainingShipModel.getMySeaRemainingSetShipModel().size();i++){//цикл по типам кораблей
            int k = 0;
            for (int j = 0; j < remainingShipModel.getMySeaRemainingSetShipModel().get(i).length; j++){//цикл по кораблям типа
                   if (remainingShipModel.getMySeaRemainingSetShipModel().get(i)[j]!=null){
                        ShipButton shipButton = new ShipButton(remainingShipModel.getMySeaRemainingSetShipModel().get(i)[j],frame,controller);//создали кнопку-корабль
                        shipButton.setMouseListener(controller);
                        this.add(shipButton,0);
                        shipButton.setLocation((j*remainingShipModel.getMySeaRemainingSetShipModel().get(i)[j].getSize()+k)*SwingConfig.WIDTH_DECK,
                                i*2*SwingConfig.HEIGHT_DECK);//указали ее координату
                    }
                    k++;
            }
        }
    }

    public void applySet(SetShipResult result){
        remainingShipModel.applySetShipResult(result);
    }

    public void applyUnset(UnSetShipResult result){
        remainingShipModel.applyUnSetShipResult(result);
    }

    public void bindController(PlacementShipButton controller) {
        this.controller = controller;
        setRemainingShip();
    }

    public void setShip(){
        System.out.println("Setting Ship");
    }
}
