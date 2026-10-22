package com;

import com.core.ListShip;
import com.displayModel.EnemySeaDisplayModel;
import com.displayModel.MySeaDisplayModel;
import com.displayModel.RemainingShipModel;
import com.displayModel.ScoreModel;

import java.io.IOException;

public interface Render {
    void renderingMySea(MySeaDisplayModel mySeaDisplayModel) throws IOException;
    void renderingEnemySea(EnemySeaDisplayModel enemySeaDisplayModel);
    void renderingMyBorder();
    void renderingEnemyBorder();
    void renderGameOver();
    void renderSetShips(ListShip listShip);
    void renderingMyRemainingShip(RemainingShipModel remainingShipModel);
    void renderScore(ScoreModel scoreModel);
}
