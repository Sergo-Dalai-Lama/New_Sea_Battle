package com;

public class ScoreModel {
    private int score;
    public ScoreModel() {
        score = 0;
    }
    public int getScore() {
        return score;
    }
    public void addScore(int score) {
        this.score += score;
    }
}
