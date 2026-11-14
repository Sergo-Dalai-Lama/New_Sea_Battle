package com.swingUI.swingBattle;

import com.swingUI.SwingConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SurrenderButton extends JButton implements ActionListener {

    public SurrenderButton() {
        setText("Surrender");
        setLayout(null);
        int x = SwingConfig.WIDTH_SEA+SwingConfig.WIDTH_SEA/2-SwingConfig.WIDTH/16;
        int y = SwingConfig.HEIGHT/2-SwingConfig.HEIGHT/10;
        setBounds(x,y,SwingConfig.WIDTH/8,SwingConfig.HEIGHT/16);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
