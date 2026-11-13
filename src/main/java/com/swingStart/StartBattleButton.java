package com.swingStart;

import com.SwingConfig;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartBattleButton extends JButton {
    public interface StartButtonListener {
        void onStartBattleButton();
    }
    private StartButtonListener startButtonListener;
    public StartBattleButton() {
        setText("Start Battle");
        setLayout(null);
        setEnabled(false);
        int width = SwingConfig.WIDTH/8;
        int x = SwingConfig.WIDTH/2;
        int y = 8*SwingConfig.HEIGHT/9;
        setBounds(x,y,SwingConfig.WIDTH/8,SwingConfig.HEIGHT/10);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                startButtonListener.onStartBattleButton();
            }
        });
    }

    public void setEnabledButton(){
        setEnabled(true);
    }
    public void setDisabledButton(){
        setEnabled(false);
    }

    public void setStartButtonListener(StartButtonListener startButtonListener) {
        this.startButtonListener = startButtonListener;
    }
}

