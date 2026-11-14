package com.swingUI.swingFinish;

import com.swingUI.swingStart.SwingStartGameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowDialogFinish extends JOptionPane {
private final int scoreResult;
private final String text;
private final SwingStartGameFrame frame;
    public  ShowDialogFinish(int scoreResult, SwingStartGameFrame frame, String text) {
        this.scoreResult = scoreResult;
        this.frame = frame;
        this.text = text;
        String scoreMessage = "You have earned "+scoreResult+" points";
        Object[] options = {"New Game","Exit"};
       int choice = showOptionDialog(
                null,
                scoreMessage,
                text,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[1]
        );
        if (choice==0){
            frame.dispose();
            SwingStartGameFrame newGame =  new SwingStartGameFrame();
        } else {
            frame.dispose();
        }
    }

}
