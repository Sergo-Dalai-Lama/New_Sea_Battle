package com.swingUI.swingBattle;

import com.swingUI.SwingConfig;
import javax.swing.*;
import java.awt.*;

public class SwingScore extends JLabel {
    public SwingScore( ) {
        setFont(new Font("Colibri", Font.BOLD, 20));
        setText("Score: "+0);
        int width = SwingConfig.WIDTH/8;
        int height = SwingConfig.HEIGHT/20;
        int x = SwingConfig.WIDTH_SEA+SwingConfig.WIDTH_SEA/2-width/2;
        int y = SwingConfig.HEIGHT/3-height/2;
        setBounds(x,y,width,height);
    }

    public void scoreUpdate(int score) {
        setText("Score: "+score);

    }

}
