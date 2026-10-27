package com;

import com.core.GameConfig;

import java.awt.*;

public class SwingConfig {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int WIDTH_SEA =  WIDTH/2;
    public static final int HEIGHT_SEA = WIDTH/2;
    public static final int WIDTH_DECK =  WIDTH_SEA/ GameConfig.ROWS;
    public static final int HEIGHT_DECK = HEIGHT_SEA/GameConfig.COLUMNS;
}
