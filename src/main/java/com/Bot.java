package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Bot {
    private final ListShip listShip;
    private final GameRules gameRules;
   // private final MySeaDisplayModel playerSeaDisplayModel;
    public Bot(/*MySeaDisplayModel playerSeaDisplayModel*/) {
     //   this.playerSeaDisplayModel = playerSeaDisplayModel;
        listShip = new ListShip();
        gameRules = new GameRules(listShip);
        gameRules.generationShipFormation();
    }
    public static final Logger logger = LoggerFactory.getLogger(Bot.class);

    public GameRules getGameRules() {
        return gameRules;
    }

    public GameState start(GameRules gameRulesPlayer) {
            GameState gameState;
            AttackOutcome attackDestroyed;
            logger.debug("Количество живых кораблей бота {}",this.listShip.sizeLive());
            do {
                gameState  = GameState.RUNNING;
                attackDestroyed = null;
                List<Coordinate> attackedCoordinatesInAttackLoop = new ArrayList<>();
                logger.debug("количество возможных координат {}", gameRulesPlayer.getUnBlockedCells().size());
                int numCoordinateFirstAttack = ThreadLocalRandom.current().nextInt(gameRulesPlayer.getUnBlockedCells().size());
                Coordinate coordinateFirstAttack = gameRulesPlayer.getUnBlockedCells().get(numCoordinateFirstAttack);
                logger.debug("Первая атака бота {}", coordinateFirstAttack);
                AttackCommand attackCommandFirst = new AttackCommand(coordinateFirstAttack);
                AttackResult attackResultFirst = gameRulesPlayer.attack(attackCommandFirst);
                logger.debug("Результат первой атаки бота {}", attackResultFirst.outcome());
            //    playerSeaDisplayModel.applyAttackResultMySea(attackResultFirst);
                if (attackResultFirst.outcome() == AttackOutcome.GAME_FINISHED) {
                    gameState = GameState.WIN;
                }
                if (attackResultFirst.outcome()==AttackOutcome.DESTROYED){attackDestroyed=AttackOutcome.DESTROYED;}
                if (attackResultFirst.outcome() == AttackOutcome.HIT) {
                    attackedCoordinatesInAttackLoop.add(coordinateFirstAttack);
                    int numCoordinateSecondAttack = ThreadLocalRandom.current().nextInt(buildAttackCross(coordinateFirstAttack).size());
                    List<Coordinate> attackCross = buildAttackCross(coordinateFirstAttack);
                    Coordinate coordinateSecondAttack = attackCross.get(numCoordinateSecondAttack);
                    logger.debug("Вторая атака бота {}", coordinateSecondAttack);
                    attackCross.remove(numCoordinateSecondAttack);
                    AttackCommand attackCommandSecond = new AttackCommand(coordinateSecondAttack);
                    AttackResult attackResultSecond = gameRulesPlayer.attack(attackCommandSecond);
                    logger.debug("Результат второй атаки бота {}", attackResultSecond.outcome());
                  //  playerSeaDisplayModel.applyAttackResultMySea(attackResultSecond);
                    if (attackResultSecond.outcome() == AttackOutcome.GAME_FINISHED) {
                        gameState = GameState.WIN;
                    }
                    if (attackResultSecond.outcome()==AttackOutcome.DESTROYED){attackDestroyed=AttackOutcome.DESTROYED;}
                    if (attackResultSecond.outcome() == AttackOutcome.HIT) {
                        attackedCoordinatesInAttackLoop.add(coordinateSecondAttack);
                        if (attackedCoordinatesInAttackLoop.getFirst().x() == attackedCoordinatesInAttackLoop.getLast().x()) {
                            gameState = cycleAttack(attackedCoordinatesInAttackLoop, gameRulesPlayer, false);
                        } else {
                            gameState = cycleAttack(attackedCoordinatesInAttackLoop, gameRulesPlayer, true);
                        }
                    }
                }
            } while (attackDestroyed==AttackOutcome.DESTROYED||gameState==GameState.SHIP_DESTROYED);
            return gameState;
    }

    private List<Coordinate> buildAttackCross(Coordinate coordinate) {
        List<Coordinate> cross = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Coordinate coordinateCurrent = new Coordinate(coordinate.x()+i,coordinate.y()+j);
                if (checkBorder(coordinate,i,j)&&(Math.abs(i)+Math.abs(j)==1)){
                    cross.add(coordinateCurrent);
                }
            }
        }
        logger.debug("Сборка креста аттак {}", cross);
        return cross;
    }
    private boolean checkBorder(Coordinate coordinate, int followingX, int followingY) {
        return  followingX+coordinate.x()>=0 && followingX+coordinate.x()<GameConfig.COLUMNS&&
                followingY+coordinate.y()>=0 && followingY+coordinate.y()<GameConfig.COLUMNS;

    }

    private GameState cycleAttack(List<Coordinate> coordinates,GameRules gameRulesPlayer,boolean dXIncrement) {
        //наименование dXIncrement не ясен, не дает представление о режиме работы метода, в следующий раз необходимо режимам методов давать четкие имена
        GameState gameState = GameState.RUNNING;
        boolean borderCollision = false;
        AttackResult attackResultFollowing;
        int dX = 0;
        int dY = 0;
        if (dXIncrement){
            dX=1;
        } else {
            dY=1;
        }
        int x = coordinates.getLast().x();
        int y = coordinates.getLast().y();
        do {
            Coordinate coordinateFollowingAttack = new Coordinate(x+dX, y+dY);
            logger.debug("Атаки в кресте {}",coordinateFollowingAttack);
            AttackCommand attackCommandFollowing = new AttackCommand(coordinateFollowingAttack);
            attackResultFollowing = gameRulesPlayer.attack(attackCommandFollowing);
            logger.debug("Результат атаки бота в кресте {}", attackResultFollowing.outcome());
          //  playerSeaDisplayModel.applyAttackResultMySea(attackResultFollowing);
            if (dXIncrement){
                dX++;
            } else {
                dY++;
            }
            if (!checkBorder(coordinateFollowingAttack,dX,dY)){
                borderCollision = true;
            }
            if (attackResultFollowing.outcome()==AttackOutcome.DESTROYED){gameState = GameState.SHIP_DESTROYED;}
            if (attackResultFollowing.outcome()==AttackOutcome.GAME_FINISHED){gameState = GameState.WIN;}
        } while (attackResultFollowing.outcome()==AttackOutcome.HIT||attackResultFollowing.outcome()==AttackOutcome.DESTROYED||!borderCollision);
        return gameState;
    }
}
