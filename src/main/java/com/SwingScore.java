package com;

import com.core.AttackResult;
import com.displayModel.ScoreModel;
import javax.swing.*;
import java.awt.*;

public class SwingScore extends JLabel {
    private final ScoreModel scoreModel;
    public SwingScore(ScoreModel scoreModel) {
        this.scoreModel = scoreModel;
        setFont(new Font("Colibri", Font.BOLD, 20));
        setText("Score: "+scoreModel.getScore());
        int width = SwingConfig.WIDTH/8;
        int height = SwingConfig.HEIGHT/20;
        int x = SwingConfig.WIDTH_SEA+SwingConfig.WIDTH_SEA/2-width/2;
        int y = SwingConfig.HEIGHT/3-height/2;
        setBounds(x,y,width,height);
    }

    public void scoreUpdate(AttackResult  result){
        scoreModel.addScore(result.scoreEarned());
        setText("Score: "+scoreModel.getScore());

    }
}
