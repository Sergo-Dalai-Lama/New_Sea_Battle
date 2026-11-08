package com;

import com.core.*;
import com.displayModel.RemainingShipModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.ColorStartGame.*;

public class NewShipButton extends JButton {
    private final SwingStartGameSea sea;
    private Container shipListContainer;
    private Container sourcePanel=null;
    private final Ship ship;
    private Orientation orientation = Orientation.HORIZONTAL;
    private final Container contentPane;
    private Point initialLocation;
    private Point offset;
    public NewShipButton(Ship ship, SwingStartGameFrame frame, SwingStartGameSea sea) {
        this.ship = ship;
        this.sea = sea;
        this.contentPane = frame.getContentPane();
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH_DECK*ship.getSize(), SwingConfig.HEIGHT_DECK);
        setBorderPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){//метод вызывается когда зажали на компоненте курсор
            sourcePanel=getParent();
            if (sourcePanel!=sea){//если объект зажали не на поле боя, то зажали на панели размещения
                shipListContainer=getParent();
            }
            initialLocation = SwingUtilities.convertPoint(//перевели из координат панели, на которой зажали
                    // и перевели в координаты contentPane
                    shipListContainer,
                    getLocation(),
                    contentPane
            );

            offset = e.getPoint();//запомнили координаты нажатого курсора на объекте
            sourcePanel.remove(NewShipButton.this);
            contentPane.add(NewShipButton.this, 0);
            setLocation(initialLocation);
            repaintRevalidate();
            }

            @Override
            public void mouseReleased(MouseEvent e){//вызывается когда мышь отпустили
                returnShip();
            }

        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){//вызывается когда курсор перемещается с зажатой клавишей на компоненте

                Point position = SwingUtilities.convertPoint(//перевели в систему координат contentPane
                        NewShipButton.this,
                        e.getPoint(),
                        contentPane
                );

                    //запомнили куда сместилась мышь на экране
                    int thisX = position.x;
                    int thisY = position.y;
                    //вычислили координаты нового положения курсора после будущего смещения
                    int nextX = thisX - offset.x;
                    int nextY = thisY - offset.y;
                    //границы поля перетаскивания
                    int minX = 0;
                    int minY = 0;
                    int maxX = SwingConfig.WIDTH-NewShipButton.this.getWidth();
                    int maxY = SwingConfig.HEIGHT-NewShipButton.this.getHeight();

                    //удерживаем nextX в пределах minX и maxX
                    nextX = Math.clamp(nextX, minX, maxX);
                    nextY = Math.clamp(nextY, minY, maxY);
                    setLocation(nextX, nextY);//применяем новые координаты курсора
            }
        });

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        Color currentColor = SHIP_DECK_OF_NON_CURSOR;
        if (getModel().isPressed()) {
            currentColor = SHIP_DECK_OF_CURSOR;
            g2d.translate(2,2);
        } else if (getModel().isRollover()) {
            currentColor = SHIP_DECK_OF_PRESSED;
        }
        drawDeck(g2d,currentColor);
        g2d.setColor(getForeground());
        g2d.dispose();

    }
    private void drawDeck(Graphics2D g2d, Color color){
            if (orientation == Orientation.HORIZONTAL) {
                for (int i = 0; i <ship.getSize(); i++) {
                    g2d.setColor(color);
                    g2d.fillRect(i*SwingConfig.WIDTH_DECK,0,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);
                    g2d.setColor(BORDER_CELL);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRect(i*SwingConfig.WIDTH_DECK,0,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);

                }
            } else  {
                for (int i = 0; i <ship.getSize(); i++) {
                    g2d.setColor(color);
                    g2d.fillRect(0,i*SwingConfig.HEIGHT_DECK,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);
                    g2d.setColor(BORDER_CELL);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRect(0,i*SwingConfig.HEIGHT_DECK,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);
                }
            }
    }
    private void repaintRevalidate(){
        contentPane.revalidate();
        contentPane.repaint();

        sea.revalidate();
        sea.repaint();

        shipListContainer.revalidate();
        shipListContainer.repaint();
    }
    private void returnShip(){//метод возвращения корабля в список
        orientation = Orientation.HORIZONTAL;
        setSize(SwingConfig.WIDTH_DECK*ship.getSize(),SwingConfig.HEIGHT_DECK);
        if (isShipOverSea(getLocation())){//здесь должны переводиться координаты в Sea
            //пока не работает, потому что не определен результат постановки на поле.
            Point sourcePosition = SwingUtilities.convertPoint(//перевести координаты в sourcePanel
                    contentPane,
                    initialLocation,
                    sea
            );
            sea.remove(NewShipButton.this);
            sourcePanel.add(NewShipButton.this, 0);
            setLocation(sourcePosition);
        } else {//здесь должны переводиться координаты в source
            Point sourcePosition = SwingUtilities.convertPoint(//перевести координаты в sourcePanel
                    contentPane,
                    initialLocation,
                    sourcePanel
            );
            contentPane.remove(NewShipButton.this);
            sourcePanel.add(NewShipButton.this, 0);
            setLocation(sourcePosition);
        }
        repaintRevalidate();


    }
    private boolean isShipOverSea(Point posInContentPane){//метод проверки нахождения над Sea
        return sea.getBounds().contains(posInContentPane);
    }
}
/*
1. Ограничить движение мыши ВЫПОЛНЕНО
2. Настроить опознавание находимся ли над панелью Sea ВЫПОЛНЕНО
3. Настроить захват корабля на поле установки в вертикальном и горизонтальном положении
4. Корректно устанавливать корабль на поле
5. Убирать корабль с поля, если взяли его с поля
6. Очищать поле от корабля, когда взяли его с поля
7. Возвращать корабль в список, если постановка после снятия некорректная
8. Возвращать корабль, если отпустили мышь не над Sea ВЫПОЛНЕНО

 */