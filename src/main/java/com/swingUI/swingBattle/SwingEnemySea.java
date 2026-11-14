package com.swingUI.swingBattle;

import com.swingUI.SwingConfig;
import com.core.*;
import com.displayModel.EnemySeaDisplayModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import static com.swingUI.swingBattle.ColorBattleGame.*;

public class SwingEnemySea extends JPanel {
    public interface EnemySeaMouseListener{
        void onClick(MouseEvent event);
        void onMoved(MouseEvent event);
        void onExted(MouseEvent event);
    }
    private EnemySeaMouseListener  enemySeaMouseListener;
    private EnemySeaDisplayModel  model;
    private final List<Point> points = new ArrayList<Point>();
    private Coordinate hoveredCell = null;
    private boolean isBlocked = false;
    public SwingEnemySea(EnemySeaDisplayModel  model) {
        this.model = model;
        setLayout(null);
        setBounds(0, 0, SwingConfig.WIDTH_SEA, SwingConfig.HEIGHT_SEA);
        setVisible(true);
        generatePoints();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                enemySeaMouseListener.onClick(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                enemySeaMouseListener.onExted(e);
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                enemySeaMouseListener.onMoved(e);
            }
        });

    }

    private void generatePoints() {
        for (int i = 0; i< GameConfig.ROWS; i++){
            for (int j = 0; j< GameConfig.COLUMNS;j++){
                Point p = new Point(i*SwingConfig.WIDTH_DECK,j*SwingConfig.HEIGHT_DECK);
                points.add(p);
            }
        }
    }


    public List<Point> getPoints() {
        return List.copyOf(points);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            paintEnemySea(g2d);
        } finally {
            g2d.dispose();
        }
    }

    private void paintEnemySea(Graphics g) {
         Graphics2D  g2d = (Graphics2D) g;
            for (int i = 0; i<GameConfig.ROWS; i++) {
                for (int j = 0; j<GameConfig.COLUMNS; j++) {
                    switch (model.getType(i,j)) {
                        case HIT:
                            if (isHoveredCell(hoveredCell,i,j)&&!isBlocked) {
                                g2d.setColor(FORBIDDEN_ATTACK);
                            } else {
                                g2d.setColor(HIT);
                            }
                            break;
                        case MISS:
                            if (isHoveredCell(hoveredCell,i,j)&&!isBlocked) {
                                g2d.setColor(FORBIDDEN_ATTACK);
                            } else {
                            g2d.setColor(MISS);
                            }
                            break;
                        case UNKNOWN: {
                                if (isHoveredCell(hoveredCell, i, j)) {
                                    if (isBlocked) {
                                        g2d.setColor(FORBIDDEN_ATTACK);
                                    } else {
                                        g2d.setColor(ALLOWED_ATTACK);
                                    }
                                } else {
                                    g2d.setColor(UNKNOWN_CELL);
                                }
                            }
                            break;
                    }
                    g2d.fillRect(i*SwingConfig.WIDTH_DECK,j*SwingConfig.HEIGHT_DECK,SwingConfig.WIDTH_DECK,SwingConfig.HEIGHT_DECK);
                }
            }
            g2d.setColor(BORDER_CELL);
            g2d.setStroke(new BasicStroke(1));
            for (int i = 0;i<=SwingConfig.HEIGHT_SEA;i+=SwingConfig.HEIGHT_DECK){
                g2d.drawLine(0,i,SwingConfig.WIDTH_SEA,i);
            }
            for (int i = 0;i<=SwingConfig.WIDTH_SEA;i+=SwingConfig.WIDTH_DECK){
                g2d.drawLine(i,0,i,SwingConfig.HEIGHT_SEA);
            }

    }

    private boolean isHoveredCell(Coordinate coordinate,int x,int y) {
        if (coordinate!=null) {
            if (coordinate.x()==x && coordinate.y()==y) {
                return hoveredCell != null && hoveredCell.equals(coordinate);
            }
        }
        return false;
    }

    public void setHoveredCell(Coordinate coordinate) {
        hoveredCell = coordinate;
    }

    public void resetHoveredCell() {
        hoveredCell = null;
    }
    public void blockAttack(){
        isBlocked = true;
        revalidate();
        repaint();
    }
    public void unBlockAttack(){
        isBlocked = false;
        revalidate();
        repaint();
    }

    public void setEnemySeaMouseListener(EnemySeaMouseListener enemySeaMouseListener){
        this.enemySeaMouseListener = enemySeaMouseListener;
    }

    public void applyAttackResult(AttackResult result){
        model.applyAttackResult(result);
        revalidate();
        repaint();
    }

}
