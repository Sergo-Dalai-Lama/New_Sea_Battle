package com;

import com.core.*;
import com.displayModel.RemainingShipModel;
import javax.swing.*;
import java.awt.*;

public class SwingStartGameRemaining extends JPanel {
    private RemainingShipModel remainingShipModel = new RemainingShipModel();
    private SwingStartGameFrame frame;
    private SwingStartGameSea gameSea;
    private SwingStartGameRemaining gameRemaining;
    public SwingStartGameRemaining(SwingStartGameFrame frame,SwingStartGameSea gameSea) {
        this.frame = frame;
        this.gameSea = gameSea;
        setLayout(null);
        setOpaque(false);
        setBounds(0,
                0,
                SwingConfig.WIDTH-SwingConfig.WIDTH_SEA,
                SwingConfig.HEIGHT_SEA);
        setRemainingShip();
    }

    private void setRemainingShip() {
        for (int i = 0; i<remainingShipModel.getMySeaRemainingSetShipModel().size();i++){//цикл по типам кораблей
            int k = 0;
            for (int j = 0; j < remainingShipModel.getMySeaRemainingSetShipModel().get(i).length; j++){//цикл по кораблям типа
                   if (remainingShipModel.getMySeaRemainingSetShipModel().get(i)[j]!=null){
                        ShipButton shipButton = new ShipButton(remainingShipModel.getMySeaRemainingSetShipModel().get(i)[j],frame,gameSea);//создали кнопку-корабль
                        this.add(shipButton);
                        shipButton.setLocation((j*remainingShipModel.getMySeaRemainingSetShipModel().get(i)[j].getSize()+k)*SwingConfig.WIDTH_DECK,
                                i*2*SwingConfig.HEIGHT_DECK);//указали ее координату
                    }
                    k++;
            }
        }
    }

    public void remove(Ship ship) {//вспомогательный метод для проверки работоспособности переноса кнопки с одного panel на другой
        for (int i = 0; i<remainingShipModel.getMySeaRemainingSetShipModel().size();i++){
            for (int j = 0; j < remainingShipModel.getMySeaRemainingSetShipModel().get(i).length; j++) {
                if (remainingShipModel.getMySeaRemainingSetShipModel().get(i)[j].equals(ship)) {
                    remainingShipModel.getMySeaRemainingSetShipModel().remove(i);
                }
            }
        }
    }

}
