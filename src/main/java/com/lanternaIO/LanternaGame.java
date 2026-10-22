package com.lanternaIO;

import com.core.*;
import com.displayModel.EnemySeaDisplayModel;
import com.displayModel.MySeaDisplayModel;
import com.displayModel.RemainingShipModel;
import com.displayModel.ScoreModel;

import java.io.IOException;
import java.util.List;

public class LanternaGame {
    static void main(String[] args) throws IOException {

    ListShip shipDynamic =  new ListShip();
    GameRules gameRules = new GameRules(shipDynamic);
    RemainingShipModel remainingShipModel = new RemainingShipModel();
    LanternaRender render = new LanternaRender();
    LanternaInput inputLanterna = new LanternaInput(render.screen);
    System.out.println("LanternaGame started");
    boolean flagSetShip1 = true;
     while (flagSetShip1) {//состояние "установка кораблей"
        //переписать код так, чтобы из циклов вытащить в отдельное состояние игры
        render.clear();
        render.renderingMyBorder();
        render.renderSetShips(shipDynamic);
        render.renderingMyRemainingShip(remainingShipModel);
        render.refresh();
        System.out.println("Ввод данных");
        ShipSetting shipSetting = inputLanterna.input();
    if (shipSetting==ShipSetting.SET){//если выбрана команда set
        System.out.println("Выбрана команда set");
        SetShipCommand command = inputLanterna.inputSetShips();
        System.out.println("Введены параметры корабля");
        SetShipResult result = gameRules.setShip(command);

        if (result.outcome() == SetShipOutcome.SET_FINISHED) {
            flagSetShip1 = false;
        }
        if (result.outcome() == SetShipOutcome.SHIP_COLLISION) {
            System.out.println("Ship collision");
        }
        if (result.outcome() == SetShipOutcome.BEYOND_FIELD) {
            System.out.println("Beyond field");
        }
        remainingShipModel.applySetShipResult(result);
    }

    if (shipSetting==ShipSetting.UNSET){//если команда убрать корабль
        System.out.println("Выбрана команда unset");
        UnSetShipCommand unSet = inputLanterna.inputUnSetShip();
        UnSetShipResult unSetResult = gameRules.unSetShip(unSet);
        remainingShipModel.applyUnSetShipResult(unSetResult);
        render.renderingMyRemainingShip(remainingShipModel);
    }
        if (shipSetting==ShipSetting.REPOSITION){//если команда убрать корабль
            System.out.println("Выбрана команда reposition");
           RepositionShipCommand repositionShipCommand = inputLanterna.inputRepositionShip();
           RepositionShipResult repositionShipResult = gameRules.repositionShip(repositionShipCommand);
           if (repositionShipResult.unSetShipResult().unSetShipOutcome()==UnSetShipOutcome.NON_UNSET){
               System.out.println("Корабль не найден");
           } else {
               if (repositionShipResult.setShipResult().outcome() == SetShipOutcome.SHIP_COLLISION) {
                   System.out.println("Ship collision");
               }
               if (repositionShipResult.setShipResult().outcome() == SetShipOutcome.BEYOND_FIELD) {
                   System.out.println("Beyond field");
               }
           }

        }

        render.renderingMyBorder();
        render.renderSetShips(shipDynamic);
        render.refresh();
        render.clear();
    }
        MySeaDisplayModel mySea1 = new MySeaDisplayModel(shipDynamic);
        EnemySeaDisplayModel enemySea2 = new EnemySeaDisplayModel();
        ScoreModel scoreModel1 = new ScoreModel();
        Bot bot = new Bot();

        while (true){//состояние "игра"
            renderGameRun(render,mySea1,enemySea2,scoreModel1);
            boolean flag1 = true;
        while (flag1) {
            AttackCommand command1 = inputLanterna.inputAttack();
            AttackResult result = bot.getGameRules().attack(command1);
            enemySea2.applyAttackResult(result);

            if (result.outcome()!=AttackOutcome.INVALID_TARGET) {
                if (result.outcome()==AttackOutcome.HIT||result.outcome()==AttackOutcome.DESTROYED){
                    scoreModel1.addScore(result.scoreEarned());
                }
                if (result.outcome()==AttackOutcome.MISS){
                    flag1 = false;
                }
                if (result.outcome()==AttackOutcome.GAME_FINISHED){
                    scoreModel1.addScore(result.scoreEarned());
                    break;
                }
                renderGameRun(render,mySea1,enemySea2,scoreModel1);
            }
        }
        List<AttackResult> results = bot.start(gameRules);
        for (AttackResult  result : results) {
            mySea1.applyAttackResultMySea(result);
        }
        if (bot.getState()==GameState.WIN){
            break;
        }
        }

        render.clear();
        render.renderGameOver();
        render.refresh();
    }

public static void renderGameRun(LanternaRender render, MySeaDisplayModel mySea1, EnemySeaDisplayModel enemySea2, ScoreModel scoreModel1) throws IOException {
    render.clear();
    render.renderingMyBorder();
    render.renderingEnemyBorder();
    render.renderScore(scoreModel1);
    render.renderingMySea(mySea1);
    render.renderingEnemySea(enemySea2);
    render.refresh();
}
}
