package com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Bot {
    private final ListShip listShip;
    private final GameRules gameRules;
    public Bot() {
        listShip = new ListShip();
        gameRules = new GameRules(listShip);
        gameRules.generationShipFormation();
    }

    public void start(GameRules gameRulesPlayer) {
            List<Coordinate> attackedCoordinatesInAttackLoop = new ArrayList<>();
            int numCoordinateFirstAttack = ThreadLocalRandom.current().nextInt(this.gameRules.getUnBlockedCells().size());
            Coordinate coordinateFirstAttack = this.gameRules.getUnBlockedCells().get(numCoordinateFirstAttack);
            AttackCommand attackCommandFirst = new AttackCommand(coordinateFirstAttack);
            AttackResult attackResultFirst = gameRulesPlayer.attack(attackCommandFirst);
            if (attackResultFirst.outcome()==AttackOutcome.HIT){
                attackedCoordinatesInAttackLoop.add(coordinateFirstAttack);
                int numCoordinateSecondAttack = ThreadLocalRandom.current().nextInt(buildAttackCross(coordinateFirstAttack).size());
                Coordinate coordinateSecondAttack = buildAttackCross(coordinateFirstAttack).get(numCoordinateSecondAttack);
                AttackCommand attackCommandSecond = new AttackCommand(coordinateFirstAttack);
                AttackResult attackResultSecond = gameRulesPlayer.attack(attackCommandSecond);
                if (attackResultSecond.outcome()==AttackOutcome.HIT){
                    boolean borderCollision = false;
                    attackedCoordinatesInAttackLoop.add(coordinateSecondAttack);
                   if (attackedCoordinatesInAttackLoop.get(0).x()==attackedCoordinatesInAttackLoop.get(1).x()){
                        AttackResult attackResultFollowing;
                        int dY = 0;
                        int numCoordinatesFollowingAttack = ThreadLocalRandom.current().nextInt(buildAttackLineVertical(attackedCoordinatesInAttackLoop).size());
                        int x = attackedCoordinatesInAttackLoop.getFirst().x();
                        //ПЕРЕПИСАТЬ АЛГОРИТМ АТАКИ ДЛЯ 4 ПАЛУБ. БУДЕТ ПРОЩЕ

                        do {
                            int y = buildAttackLineVertical(attackedCoordinatesInAttackLoop).get(numCoordinatesFollowingAttack).y();
                            Coordinate coordinateFollowingAttack = new Coordinate(x, y+dY);
                            attackedCoordinatesInAttackLoop.add(new Coordinate(x, y+dY));
                            AttackCommand attackCommandFollowing = new AttackCommand(coordinateFollowingAttack);
                            attackResultFollowing = gameRulesPlayer.attack(attackCommandFollowing);
                            dY++;
                            if (!checkBorder(coordinateFollowingAttack,0,dY)){
                                borderCollision = true;
                            }
                        } while (attackResultFollowing.outcome()==AttackOutcome.HIT||attackResultFollowing.outcome()==AttackOutcome.DESTROYED||!borderCollision);
                        if (attackResultFollowing.outcome()==AttackOutcome.MISS){
                            AttackResult attackResultReturn;
                            int dYReturn = 0;
                            int yReturn = attackedCoordinatesInAttackLoop.getLast().y()-attackedCoordinatesInAttackLoop.size();
                            int xReturn = attackedCoordinatesInAttackLoop.getFirst().x();
                            do {
                                Coordinate coordinateReturnAttack = new Coordinate(xReturn,yReturn+dYReturn);
                                AttackCommand attackCommandReturn = new AttackCommand(coordinateReturnAttack);
                                attackResultReturn = gameRulesPlayer.attack(attackCommandReturn);

                            } while (attackResultReturn.outcome()==AttackOutcome.HIT);
                        }
                   } else {
                       //ЗДЕСЬ ДОПИСАТЬ АЛГОРИТМ АТАКИ ПО ГОРИЗОНТАЛИ АНАЛОГИЧНО ВЕРТИКАЛЬНОМУ АЛГОРИТМУ
                       AttackResult attackResultFollowing;
                       int dX = 0;
                       int numCoordinatesFollowingAttack = ThreadLocalRandom.current().nextInt(buildAttackLineHorizontal(attackedCoordinatesInAttackLoop).size());
                       int y = buildAttackLineHorizontal(attackedCoordinatesInAttackLoop).get(numCoordinatesFollowingAttack).y();
                       do {
                           int x = buildAttackLineHorizontal(attackedCoordinatesInAttackLoop).get(numCoordinatesFollowingAttack).x();
                           Coordinate coordinateFollowingAttack = new Coordinate(x+dX, y);
                           attackedCoordinatesInAttackLoop.add(new Coordinate(x+dX, y));
                           AttackCommand attackCommandFollowing = new AttackCommand(coordinateFollowingAttack);
                           attackResultFollowing = gameRulesPlayer.attack(attackCommandFollowing);
                           dX++;
                           if (!checkBorder(coordinateFollowingAttack,dX,0)){
                               borderCollision = true;
                           }
                       } while (attackResultFollowing.outcome()==AttackOutcome.HIT||attackResultFollowing.outcome()==AttackOutcome.DESTROYED||!borderCollision);
                   }
                }
            }
    }

    private List<Coordinate> buildAttackCross(Coordinate coordinate) {
        List<Coordinate> cross = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (checkBorder(coordinate,i,j)&&i!=j){
                    cross.add(coordinate);
                }
            }
        }

        return cross;
    }
    private boolean checkBorder(Coordinate coordinate, int followingX, int followingY) {
        return  followingX+coordinate.x()>=0 && followingX+coordinate.x()<GameConfig.COLUMNS&&
                followingY+coordinate.y()>=0 && followingY+coordinate.y()<GameConfig.COLUMNS;

    }

    private List<Coordinate> buildAttackLineHorizontal(List<Coordinate> hitCoordinates) {
        List<Coordinate> lineCells = new ArrayList<>();
        if (hitCoordinates.get(0).x()-hitCoordinates.get(1).x()<0){
            if (checkBorder(hitCoordinates.getFirst(),-1,0)){
                lineCells.add(new Coordinate(hitCoordinates.getFirst().x()-1,hitCoordinates.getFirst().y()));
            }
        } else {
            if (checkBorder(hitCoordinates.get(1),1,0)){
                lineCells.add(new Coordinate(hitCoordinates.get(1).x()+1,hitCoordinates.get(1).y()));
            }
        }
        return lineCells;
    }
    private List<Coordinate> buildAttackLineVertical(List<Coordinate> hitCoordinates) {
        List<Coordinate> lineCells = new ArrayList<>();
        if (hitCoordinates.get(0).y()-hitCoordinates.get(1).y()<0){
            if (checkBorder(hitCoordinates.getFirst(),0,-1)){
                lineCells.add(new Coordinate(hitCoordinates.getFirst().x(),hitCoordinates.getFirst().y()-1));
            }
        } else {
            if (checkBorder(hitCoordinates.get(1),0,1)){
                lineCells.add(new Coordinate(hitCoordinates.get(1).x(),hitCoordinates.get(1).y()+1));
            }
        }
        return lineCells;
    }

}
