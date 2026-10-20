package com;

import java.io.IOException;

public class Main{
static void main() throws IOException {

    ListShip shipDynamic1 =  new ListShip();//список для 1 игрока
    GameRules gameRules1 = new GameRules(shipDynamic1);//ядро для 1 игрока
    RemainingShipModel remainingShipModel1 = new RemainingShipModel();
    ListShip shipDynamic2 =  new ListShip();//список для 2 игрока
    GameRules gameRules2 = new GameRules(shipDynamic2);//ядро для 2 игрока
    LanternaRender render = new LanternaRender();//создал отрисовку экрана
    LanternaInput inputLanterna = new LanternaInput(render.screen);//создали класс вводимых данных, связанных с экраном отрисовки
    System.out.println("Начало игрового цикла");

    boolean flagSetShip1 = true ;
    while (flagSetShip1) {//состояние "установка кораблей"
        //переписать код так, чтобы из циклов вытащить в отдельное состояние игры
        render.clear();
        render.renderingMyBorder();
        render.renderSetShips(shipDynamic1);
        render.renderingMyRemainingShip(remainingShipModel1);
        render.refresh();
        System.out.println("Ввод данных");
        ShipSetting shipSetting = inputLanterna.input();
    if (shipSetting==ShipSetting.SET){//если выбрана команда set
        System.out.println("Выбрана команда set");
        SetShipCommand command1 = inputLanterna.inputSetShips();
        System.out.println("Введены параметры корабля");
        SetShipResult result1 = gameRules1.setShip(command1);

        if (result1.outcome() == SetShipOutcome.SET_FINISHED) {
            flagSetShip1 = false;
        }
        if (result1.outcome() == SetShipOutcome.SHIP_COLLISION) {
            System.out.println("Ship collision");
        }
        if (result1.outcome() == SetShipOutcome.BEYOND_FIELD) {
            System.out.println("Beyond field");
        }
        remainingShipModel1.applySetShipResult(result1);
    }

    if (shipSetting==ShipSetting.UNSET){//если команда убрать корабль
        System.out.println("Выбрана команда unset");
        UnSetShipCommand unSet = inputLanterna.inputUnSetShip();
        UnSetShipResult unSetResult = gameRules1.unSetShip(unSet);
        remainingShipModel1.applyUnSetShipResult(unSetResult);
        render.renderingMyRemainingShip(remainingShipModel1);
    }
        if (shipSetting==ShipSetting.REPOSITION){//если команда убрать корабль
            System.out.println("Выбрана команда reposition");
           RepositionShipCommand repositionShipCommand = inputLanterna.inputRepositionShip();
           RepositionShipResult repositionShipResult = gameRules1.repositionShip(repositionShipCommand);
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
        render.renderSetShips(shipDynamic1);
        render.refresh();
        render.clear();
    }

    System.out.println("Generation ship");
    gameRules2.generationShipFormation();//сгенерировали поле для 2 игрока

    EnemySeaDisplayModel enemySea1 = new EnemySeaDisplayModel();
    MySeaDisplayModel mySea1 =  new MySeaDisplayModel(shipDynamic1);
    EnemySeaDisplayModel enemySea2 = new EnemySeaDisplayModel();
    MySeaDisplayModel mySea2 =  new MySeaDisplayModel(shipDynamic2);
    ScoreModel scoreModel1 = new ScoreModel();
    ScoreModel scoreModel2 = new ScoreModel();
    System.out.println("RUN");
    boolean flag = true;
    while (flag){//состояние "игра"
        renderGameRun(render,mySea1,enemySea2,scoreModel1);
        boolean flag1 = true;
        while (flag1) {
            AttackCommand command1 = inputLanterna.inputAttack();
            AttackResult result1 = gameRules2.attack(command1);
            mySea2.applyAttackResultMySea(result1);
            enemySea2.applyAttackResult(result1);
            if (result1.outcome()==AttackOutcome.HIT||result1.outcome()==AttackOutcome.DESTROYED){
                scoreModel1.addScore(result1.scoreEarned());
            }
            if (result1.outcome()==AttackOutcome.MISS){
                flag1 = false;
            }
            if (result1.outcome()==AttackOutcome.GAME_FINISHED){
                scoreModel1.addScore(result1.scoreEarned());
                flag = false;
                break;
            }
            renderGameRun(render,mySea1,enemySea2,scoreModel1);
        }
        renderGameRun(render,mySea2,enemySea1,scoreModel2);
        boolean flag2 = true;
        while (flag2) {
            AttackCommand command2 = inputLanterna.inputAttack();
            AttackResult result2 = gameRules1.attack(command2);
            mySea1.applyAttackResultMySea(result2);
            enemySea1.applyAttackResult(result2);
            if (result2.outcome()==AttackOutcome.HIT||result2.outcome()==AttackOutcome.DESTROYED){
                scoreModel2.addScore(result2.scoreEarned());
            }
            if (result2.outcome()==AttackOutcome.MISS){
                flag2 = false;
            }
            if (result2.outcome()==AttackOutcome.GAME_FINISHED){
                scoreModel2.addScore(result2.scoreEarned());
                flag = false;
                break;
            }
            renderGameRun(render,mySea2,enemySea1,scoreModel2);
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
