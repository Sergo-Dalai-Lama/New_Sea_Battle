package com;

import com.core.GameRules;
import com.core.ListShip;
import com.displayModel.MySeaDisplayModel;
import com.displayModel.RemainingShipModel;

import javax.swing.*;
import java.awt.*;
import static com.ColorStartGame.BACKGROUND;

//запомнить порядок настройки окна
public class SwingStartGameFrame extends JFrame {
private final GameRules  gameRules;
private final ListShip listShip;
private final RemainingShipModel remainingShipModel = new RemainingShipModel();
    public SwingStartGameFrame() {
        listShip = new ListShip();
        gameRules = new GameRules(listShip);
        MySeaDisplayModel mySeaDisplayModel = new MySeaDisplayModel(listShip);
        setTitle("New Sea Battle");//назначить имя окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//назначить по умолчанию закрытие окна
        Container contentPane = getContentPane();//назначить базовое основание для расположения объектов
        getContentPane().setLayout(null);//назначить тип расположения окон относительно основания
        contentPane.setPreferredSize(new Dimension(SwingConfig.WIDTH, SwingConfig.HEIGHT));//задать желаемые размеры основания
        contentPane.setBackground(BACKGROUND);//если просто задать цвет фрейма, то ничего не поменяется, так как contentPane перекрывает его
        pack();//установить подбор размера окна только после настройки всех параметров основания
        //можно сказать, что параметры основания принимаются только после pack()
        SwingStartGameSea swingStartGameSea = new SwingStartGameSea(listShip, gameRules);
        int y = (getContentPane().getHeight() - SwingConfig.HEIGHT_SEA) / 2;//рассчитывать другие координаты относительно основания можно только после установки
        //размеров основания
        swingStartGameSea.setLocation(0, y);//работает если указать setBounds непосредственно в объекте
        contentPane.add(swingStartGameSea);//добавить панель на основание только после инициализации и настройки основания
        SwingStartGameRemaining swingStartGameRemaining = new SwingStartGameRemaining(this,swingStartGameSea);
        int x = getContentPane().getWidth() - SwingConfig.WIDTH_SEA + SwingConfig.WIDTH_DECK;
        swingStartGameRemaining.setLocation(x, y);
        contentPane.add(swingStartGameRemaining);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
