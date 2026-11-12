package com;

import com.core.GameRules;
import com.core.ListShip;
import javax.swing.*;
import java.awt.*;
import static com.ColorStartGame.BACKGROUND;

//запомнить порядок настройки окна
public class SwingStartGameFrame extends JFrame {
private final GameRules  gameRules;
public final ListShip listShip;

    public SwingStartGameFrame() {
        listShip = new ListShip();
        gameRules = new GameRules(listShip);
        setTitle("New Sea Battle");//назначить имя окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//назначить по умолчанию закрытие окна
        Container contentPane = getContentPane();//назначить базовое основание для расположения объектов
        getContentPane().setLayout(null);//назначить тип расположения окон относительно основания
        contentPane.setPreferredSize(new Dimension(SwingConfig.WIDTH, SwingConfig.HEIGHT));//задать желаемые размеры основания
        contentPane.setBackground(BACKGROUND);//если просто задать цвет фрейма, то ничего не поменяется, так как contentPane перекрывает его
        pack();//установить подбор размера окна только после настройки всех параметров основания
        //можно сказать, что параметры основания принимаются только после pack()
        StartBattleButton startBattleButton = new StartBattleButton();
        contentPane.add(startBattleButton);
        GenerateSetShipButton generateSetShipButton = new GenerateSetShipButton();
        contentPane.add(generateSetShipButton);
        int y = (getContentPane().getHeight() - SwingConfig.HEIGHT_SEA) / 2;//рассчитывать другие координаты относительно основания можно только после установки
        //размеров основания
        SwingStartGameSea sea = new SwingStartGameSea(gameRules);
        sea.setLocation(0, y);//работает если указать setBounds непосредственно в объекте
        contentPane.add(sea);//добавить панель на основание только после инициализации и настройки основания
        SwingStartGameRemaining remaining = new SwingStartGameRemaining();
        int x = getContentPane().getWidth() - SwingConfig.WIDTH_SEA + SwingConfig.WIDTH_DECK;
        remaining.setLocation(x, y);
        contentPane.add(remaining);
        ControllerPlacement placementController = new ControllerPlacement(sea, remaining,startBattleButton,generateSetShipButton,contentPane,gameRules);
        remaining.bindController(placementController);
        ControllerStartButton startGame = new ControllerStartButton(contentPane,listShip,gameRules,this);
        startBattleButton.setStartButtonListener(startGame);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
