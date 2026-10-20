package com;

public class EnemySeaDisplayModel {

    private TypeCellEnemy[][] enemySea;
    public EnemySeaDisplayModel() {
        fieldGenerate();
    }

    private void fieldGenerate() {
        enemySea = new TypeCellEnemy[GameConfig.ROWS][GameConfig.COLUMNS];
        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLUMNS; j++) {
                enemySea[i][j] = TypeCellEnemy.UNKNOWN;
            }
        }
    }

    public TypeCellEnemy getType(int x, int y) {//пока для отладки метод
        return enemySea[x][y];
    }//вспомогательный метод

    public void applyAttackResult(AttackResult result) {

        if (result.outcome()==AttackOutcome.INVALID_TARGET) {
            return;
        }

        if (result.hit()) {
            enemySea[result.coordinate().x()][result.coordinate().y()]=TypeCellEnemy.HIT;
            if (result.targetDestroyed()) {
                for (Coordinate coordinate : result.targetsCoordinates()) {
                    for (int j = -1; j < 2; j++) {
                        for (int k = -1; k < 2; k++) {
                            if (coordinate.x() + j >= 0
                                    && coordinate.x() + j < GameConfig.ROWS
                                    && coordinate.y() + k >= 0
                                    && coordinate.y() + k < GameConfig.COLUMNS) {
                                if (enemySea[coordinate.x() + j][coordinate.y() + k] == TypeCellEnemy.UNKNOWN) {
                                    enemySea[coordinate.x() + j][coordinate.y() + k] = TypeCellEnemy.MISS;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            enemySea[result.coordinate().x()][result.coordinate().y()]=TypeCellEnemy.MISS;
        }
    }

}
