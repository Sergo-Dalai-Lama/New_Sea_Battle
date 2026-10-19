import com.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class GameRulesTests {
    private ListShip listShip;
    private GameRules gameRules;

    @BeforeEach
    public void setup() {
        listShip = new ListShip();
        gameRules = new GameRules(listShip);
    }

    @ParameterizedTest
    @CsvSource({
            "9,0",
            "9,9",
            "0,9"
    })
    public void testUnSetShip_returnUnset(int x, int y) {
        //Arrange
        Coordinate coordinateSet = new Coordinate(x,y);
        Coordinate coordinateUnSet = new Coordinate(x,y);
        SetShipCommand setShipCommand = new SetShipCommand(coordinateSet,Ship.PATROL1,Orientation.HORIZONTAL);
        gameRules.setShip(setShipCommand);
        //Act
        UnSetShipCommand unSetShipCommand = new UnSetShipCommand(coordinateUnSet);
        UnSetShipResult unSetShipResult = gameRules.unSetShip(unSetShipCommand);
        //Assert
        assertEquals(UnSetShipOutcome.UNSET,unSetShipResult.unSetShipOutcome());//сначала пишем какой результат ожидаем, а потом какой получаем
        assertEquals(Ship.PATROL1,unSetShipResult.ship());
        assertFalse(listShip.isShipOccupied(Ship.PATROL1));
    }
    @Test
    public void testUnSetShip_returnNonUnset() {
        //Arrange
        Coordinate coordinateSet = new Coordinate(1,1);
        Coordinate coordinateUnSet = new Coordinate(2,2);
        SetShipCommand setShipCommand = new SetShipCommand(coordinateSet,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameRules.setShip(setShipCommand);
        //Act
        UnSetShipCommand unSetShipCommand = new UnSetShipCommand(coordinateUnSet);
        UnSetShipResult unSetShipResult = gameRules.unSetShip(unSetShipCommand);
        //Assert
        assertEquals(UnSetShipOutcome.NON_UNSET,unSetShipResult.unSetShipOutcome());
        assertNull(unSetShipResult.ship());
        assertTrue(listShip.isShipOccupied(Ship.BATTLESHIP1));
    }
    @Test
    public void testSet_returnSet() {

        Coordinate coordinateSet = new Coordinate(1,1);

        SetShipCommand setShipCommand =  new SetShipCommand(coordinateSet,Ship.BATTLESHIP1,Orientation.HORIZONTAL);

        assertEquals(SetShipOutcome.SET, gameRules.setShip(setShipCommand).outcome());
        assertTrue(listShip.isShipOccupied(Ship.BATTLESHIP1));

    }
    @Test
    public void testSet_returnBeyondField() {

        Coordinate coordinateSet = new Coordinate(7,7);

        SetShipCommand setShipCommand =  new SetShipCommand(coordinateSet,Ship.BATTLESHIP1,Orientation.HORIZONTAL);

        assertEquals(SetShipOutcome.BEYOND_FIELD, gameRules.setShip(setShipCommand).outcome());
        assertFalse(listShip.isShipOccupied(Ship.BATTLESHIP1));
    }
    @ParameterizedTest
    @CsvSource({
            "0,0",
            "9,0",
            "9,9",
            "0,9"
    })
    public void testSet_returnSet_near_border(int x, int y) {

        Coordinate coordinateSet = new Coordinate(x,y);

        SetShipCommand setShipCommand =  new SetShipCommand(coordinateSet,Ship.PATROL1,Orientation.HORIZONTAL);

        assertEquals(SetShipOutcome.SET, gameRules.setShip(setShipCommand).outcome());
        assertTrue(listShip.isShipOccupied(Ship.PATROL1));
    }
    @Test
    public void testSet_returnAvailable_doubleTry() {

        Coordinate coordinateSet1 = new Coordinate(1,1);
        Coordinate coordinateSet2 = new Coordinate(5,5);
        SetShipCommand setShipCommand1 =  new SetShipCommand(coordinateSet1,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameRules.setShip(setShipCommand1);

        SetShipCommand setShipCommand2 = new SetShipCommand(coordinateSet2,Ship.BATTLESHIP1,Orientation.VERTICAL);
        gameRules.setShip(setShipCommand2);

        assertEquals(SetShipOutcome.SHIP_IS_AVAILABLE, gameRules.setShip(setShipCommand2).outcome());
        assertTrue(listShip.isShipOccupied(coordinateSet1));
        assertFalse(listShip.isShipOccupied(coordinateSet2));
    }
    @ParameterizedTest
    @CsvSource({
            "10,0",
            "10,10",
            "0,10",
            "-1,0",
            "0,-1"
    })
    public void testSet_returnBeyondField_near_border(int x, int y) {

        Coordinate coordinateSet = new Coordinate(x,y);

        SetShipCommand setShipCommand =  new SetShipCommand(coordinateSet,Ship.PATROL1,Orientation.HORIZONTAL);

        assertEquals(SetShipOutcome.BEYOND_FIELD, gameRules.setShip(setShipCommand).outcome());
        assertFalse(listShip.isShipOccupied(Ship.PATROL1));
    }

    @Test
    public void testReposition_returnSet() {

        Coordinate coordinateSet1 = new Coordinate(1,1);
        Coordinate coordinateSet2 = new Coordinate(2,2);
        SetShipCommand setShipCommand = new SetShipCommand(coordinateSet1,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameRules.setShip(setShipCommand);

        RepositionShipResult result = gameRules.repositionShip(new RepositionShipCommand(coordinateSet1,coordinateSet2,Orientation.HORIZONTAL));

        assertEquals(SetShipOutcome.SET,result.setShipResult().outcome());
        assertTrue(listShip.isShipOccupied(Ship.BATTLESHIP1));
    }

    @Test
    public void testReposition_returnNonUnset() {

        Coordinate coordinateSet1 = new Coordinate(1,1);
        Coordinate coordinateSet2 = new Coordinate(2,2);
        Coordinate coordinateSet3 = new Coordinate(3,3);
        SetShipCommand setShipCommand = new SetShipCommand(coordinateSet1,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameRules.setShip(setShipCommand);

        RepositionShipResult result = gameRules.repositionShip(new RepositionShipCommand(coordinateSet2,coordinateSet3,Orientation.HORIZONTAL));

        assertEquals(UnSetShipOutcome.NON_UNSET,result.unSetShipResult().unSetShipOutcome());
        assertEquals(SetShipOutcome.INVALID_DATA,result.setShipResult().outcome());
        assertTrue(listShip.isShipOccupied(coordinateSet1));
        assertFalse(listShip.isShipOccupied(coordinateSet3));
    }

    @Test
    public void testReposition_returnNonSet() {

        Coordinate coordinateSet1 = new Coordinate(1,1);
        Coordinate coordinateSet2 = new Coordinate(8,8);
        SetShipCommand setShipCommand = new SetShipCommand(coordinateSet1,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameRules.setShip(setShipCommand);

        RepositionShipResult result = gameRules.repositionShip(new RepositionShipCommand(coordinateSet1,coordinateSet2,Orientation.HORIZONTAL));

        assertEquals(UnSetShipOutcome.UNSET,result.unSetShipResult().unSetShipOutcome());
        assertEquals(SetShipOutcome.BEYOND_FIELD,result.setShipResult().outcome());
        assertTrue(listShip.isShipOccupied(coordinateSet1));
        assertFalse(listShip.isShipOccupied(coordinateSet2));
    }

    @ParameterizedTest
    @CsvSource({
            "-1,0",
            "10,0",
            "0,-1",
            "10,10",
            "100,200"
    })
    public void testAttack_returnInvalidTarget(int x, int y) {

        EnemySeaDisplayModel enemySea = new EnemySeaDisplayModel();

        AttackResult result = gameRules.attack(new AttackCommand(new Coordinate(x,y)));
        enemySea.applyAttackResult(result);

        assertEquals(AttackOutcome.INVALID_TARGET, result.outcome());

    }

    @ParameterizedTest
    @CsvSource({
            "1,1",
            "2,1",
            "3,1",
            "4,1"
    })
    public void testAttack_returnHit(int x, int y) {

        SetShipCommand setShipCommand = new SetShipCommand(new Coordinate(1,1),Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameRules.setShip(setShipCommand);
        EnemySeaDisplayModel enemySea = new EnemySeaDisplayModel();

        AttackResult result = gameRules.attack(new AttackCommand(new Coordinate(x,y)));
        enemySea.applyAttackResult(result);

        assertEquals(AttackOutcome.HIT, result.outcome());
        assertEquals(TypeCellEnemy.HIT,enemySea.getType(x,y));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "9,9",
            "0,9",
            "9,0"
    })
    public void testAttack_returnMiss(int x, int y) {

        EnemySeaDisplayModel enemySea = new EnemySeaDisplayModel();

        AttackResult result = gameRules.attack(new AttackCommand(new Coordinate(x,y)));
        enemySea.applyAttackResult(result);

        assertEquals(AttackOutcome.MISS, result.outcome());
        assertEquals(TypeCellEnemy.MISS,enemySea.getType(x,y));
    }

    @ParameterizedTest
    @EnumSource(Orientation.class)
    public void testAttack_marksKilledShip(Orientation orientation) {

        SetShipCommand setShipCommand = new SetShipCommand(new Coordinate(3,3),Ship.BATTLESHIP1,orientation);

        gameRules.setShip(setShipCommand);
        EnemySeaDisplayModel enemySea = new EnemySeaDisplayModel();

        AttackResult result = null;
        for (int i = 0; i < Ship.BATTLESHIP1.getSize(); i++) {
            if (orientation == Orientation.HORIZONTAL) {
                result = gameRules.attack(new AttackCommand(new Coordinate(3+i,3)));
                enemySea.applyAttackResult(result);
            }  else {
                result = gameRules.attack(new AttackCommand(new Coordinate(3,3+i)));
                enemySea.applyAttackResult(result);
            }
        }
        for (int i = 0; i < Ship.BATTLESHIP1.getSize(); i++) {
            if (orientation == Orientation.HORIZONTAL) {
                assertEquals(TypeCellEnemy.HIT,enemySea.getType(3+i,3));
            }  else {
                assertEquals(TypeCellEnemy.HIT,enemySea.getType(3,3+i));
            }
        }

    }

    @RepeatedTest(100)
    public void testGenerationShip_resultNotNull() {

        gameRules.generationShipFormation();

        MySeaDispayModel mySeaDispayModel = new MySeaDispayModel(listShip);

        assertEquals(10,listShip.sizeLive());
        for (int x = 0; x < GameConfig.ROWS; x++) {
            for (int y = 0; y < GameConfig.COLUMNS; y++) {
                assertNotNull(mySeaDispayModel.getType(x, y));
            }
        }
    }

    @RepeatedTest(200)
    public void testFieldGenerationShip() {

        gameRules.generationShipFormation();

        MySeaDispayModel mySeaDispayModel = new MySeaDispayModel(listShip);

        for (int x = 0; x < GameConfig.ROWS; x++) {
            for (int y = 0; y < GameConfig.COLUMNS; y++) {
                if (mySeaDispayModel.getType(x,y)==TypeCellMySea.DECK) {
                    assertTrue(mySeaDispayModel.getType(x, y) == TypeCellMySea.DECK && listShip.isShipOccupied(new Coordinate(x, y)));
                } else {
                    assertTrue(mySeaDispayModel.getType(x, y) == TypeCellMySea.SEA && !listShip.isShipOccupied(new Coordinate(x, y)));
                }
            }
        }
    }

    @Test
    public void testFieldGenerationNoShip() {

        MySeaDispayModel mySeaDispayModel = new MySeaDispayModel(listShip);

        for (int x = 0; x < GameConfig.ROWS; x++) {
            for (int y = 0; y < GameConfig.COLUMNS; y++) {
                    assertEquals(TypeCellMySea.SEA,mySeaDispayModel.getType(x, y));
            }
        }
    }
}
