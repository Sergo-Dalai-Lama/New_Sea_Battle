package com.swingUI.swingStart;

import com.core.GameRules;
import com.core.ListShip;
import com.swingUI.SwingConfig;

import javax.swing.*;
import java.awt.*;
import static com.swingUI.swingStart.ColorStartGame.BACKGROUND;

//запомнить порядок настройки окна
public class SwingStartGameFrame extends JFrame {
private final GameRules  gameRules;
public final ListShip listShip;

    public SwingStartGameFrame() {
        listShip = new ListShip();
        gameRules = new GameRules(listShip);
        setTitle("New Sea Battle");//назначить имя окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//назначить по умолчанию закрытие окна
        getContentPane().setLayout(null);//назначить тип расположения окон относительно основания
        getContentPane().setPreferredSize(new Dimension(SwingConfig.WIDTH, SwingConfig.HEIGHT));//задать желаемые размеры основания
        getContentPane().setBackground(BACKGROUND);//если просто задать цвет фрейма, то ничего не поменяется, так как contentPane перекрывает его
        pack();//установить подбор размера окна только после настройки всех параметров основания
        //можно сказать, что параметры основания принимаются только после pack()
        SwingStartGamePanel swingStartGamePanel = new SwingStartGamePanel(gameRules,listShip,this);
        add(swingStartGamePanel);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

}
