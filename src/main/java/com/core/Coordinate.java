package com.core;

public record Coordinate (int x, int y) {// создал отдельный класс координат для читаемости, так как не понятно, что значит Integer[]{}
@Override
    public String toString() {//переопределили метод для красивого отображения в println()
    return "x: " + x + " y: " + y+"\n";
}
}
