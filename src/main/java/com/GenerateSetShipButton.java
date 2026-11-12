package com;

import javax.swing.*;
public class GenerateSetShipButton extends JButton {

    public GenerateSetShipButton() {
        setText("Generate");
        int width = SwingConfig.WIDTH/8;
        int x = SwingConfig.WIDTH/2-width;
        int y = 8*SwingConfig.HEIGHT/9;
        setBounds(x,y,SwingConfig.WIDTH/8,SwingConfig.HEIGHT/10);

    }

}
