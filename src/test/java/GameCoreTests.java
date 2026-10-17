import com.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameCoreTests {
    @Test
    public void testUnSetShip_returnUnset() {
        //Arrange
        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        Coordinate coordinateSet = new Coordinate(1,1);
        Coordinate coordinateUnSet = new Coordinate(2,1);
        SetShipCommand setShipCommand = new SetShipCommand(coordinateSet,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameCore.setShip(setShipCommand);
        //Act
        UnSetShipCommand unSetShipCommand = new UnSetShipCommand(coordinateUnSet);
        UnSetShipResult unSetShipResult = gameCore.unSetShip(unSetShipCommand);
        //Assert
        assertEquals(UnSetShipOutcome.UNSET,unSetShipResult.unSetShipOutcome());//сначала пишем какой результат ожидаем, а потом какой получаем
        assertEquals(Ship.BATTLESHIP1,unSetShipResult.ship());
        assertFalse(listShip.isShipOccupied(Ship.BATTLESHIP1));
    }
    @Test
    public void testUnSetShip_returnNonUnset() {
        //Arrange
        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        Coordinate coordinateSet = new Coordinate(1,1);
        Coordinate coordinateUnSet = new Coordinate(2,2);
        SetShipCommand setShipCommand = new SetShipCommand(coordinateSet,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameCore.setShip(setShipCommand);
        //Act
        UnSetShipCommand unSetShipCommand = new UnSetShipCommand(coordinateUnSet);
        UnSetShipResult unSetShipResult = gameCore.unSetShip(unSetShipCommand);
        //Assert
        assertEquals(UnSetShipOutcome.NON_UNSET,unSetShipResult.unSetShipOutcome());
        assertNull(unSetShipResult.ship());
        assertTrue(listShip.isShipOccupied(Ship.BATTLESHIP1));
    }
    @Test
    public void testSet_returnSet() {

        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        Coordinate coordinateSet = new Coordinate(1,1);

        SetShipCommand setShipCommand =  new SetShipCommand(coordinateSet,Ship.BATTLESHIP1,Orientation.HORIZONTAL);

        assertEquals(SetShipOutcome.SET,gameCore.setShip(setShipCommand).outcome());
        assertTrue(listShip.isShipOccupied(Ship.BATTLESHIP1));

    }
    @Test
    public void testSet_returnBeyondField() {

        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        Coordinate coordinateSet = new Coordinate(7,7);

        SetShipCommand setShipCommand =  new SetShipCommand(coordinateSet,Ship.BATTLESHIP1,Orientation.HORIZONTAL);

        assertEquals(SetShipOutcome.BEYOND_FIELD,gameCore.setShip(setShipCommand).outcome());
        assertFalse(listShip.isShipOccupied(Ship.BATTLESHIP1));
    }
    @Test
    public void testSet_returnSet_near_border() {

        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        Coordinate coordinateSet = new Coordinate(6,6);

        SetShipCommand setShipCommand =  new SetShipCommand(coordinateSet,Ship.BATTLESHIP1,Orientation.HORIZONTAL);

        assertEquals(SetShipOutcome.SET,gameCore.setShip(setShipCommand).outcome());
        assertTrue(listShip.isShipOccupied(Ship.BATTLESHIP1));
    }
    @Test
    public void testSet_returnAvailable_doubleTry() {

        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        Coordinate coordinateSet1 = new Coordinate(1,1);
        Coordinate coordinateSet2 = new Coordinate(5,5);
        SetShipCommand setShipCommand1 =  new SetShipCommand(coordinateSet1,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameCore.setShip(setShipCommand1);

        SetShipCommand setShipCommand2 = new SetShipCommand(coordinateSet2,Ship.BATTLESHIP1,Orientation.VERTICAL);
        gameCore.setShip(setShipCommand2);

        assertEquals(SetShipOutcome.SHIP_IS_AVAILABLE,gameCore.setShip(setShipCommand2).outcome());
        assertTrue(listShip.isShipOccupied(coordinateSet1));
        assertFalse(listShip.isShipOccupied(coordinateSet2));
    }
    @Test
    public void testSet_returnBeyondField_near_border() {

        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        Coordinate coordinateSet = new Coordinate(10,0);

        SetShipCommand setShipCommand =  new SetShipCommand(coordinateSet,Ship.BATTLESHIP1,Orientation.HORIZONTAL);

        assertEquals(SetShipOutcome.BEYOND_FIELD,gameCore.setShip(setShipCommand).outcome());
        assertFalse(listShip.isShipOccupied(Ship.BATTLESHIP1));
    }

    @Test
    public void testReposition_returnSet() {
        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        Coordinate coordinateSet1 = new Coordinate(1,1);
        Coordinate coordinateSet2 = new Coordinate(2,2);
        SetShipCommand setShipCommand = new SetShipCommand(coordinateSet1,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameCore.setShip(setShipCommand);

        RepositionShipResult result = gameCore.repositionShip(new RepositionShipCommand(coordinateSet1,coordinateSet2,Orientation.HORIZONTAL));

        assertEquals(SetShipOutcome.SET,result.setShipResult().outcome());
        assertTrue(listShip.isShipOccupied(Ship.BATTLESHIP1));
    }

    @Test
    public void testReposition_returnNonUnset() {
        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        Coordinate coordinateSet1 = new Coordinate(1,1);
        Coordinate coordinateSet2 = new Coordinate(2,2);
        Coordinate coordinateSet3 = new Coordinate(3,3);
        SetShipCommand setShipCommand = new SetShipCommand(coordinateSet1,Ship.BATTLESHIP1,Orientation.HORIZONTAL);
        gameCore.setShip(setShipCommand);

        RepositionShipResult result = gameCore.repositionShip(new RepositionShipCommand(coordinateSet2,coordinateSet3,Orientation.HORIZONTAL));

        assertEquals(UnSetShipOutcome.NON_UNSET,result.unSetShipResult().unSetShipOutcome());
        assertEquals(SetShipOutcome.INVALID_DATA,result.setShipResult().outcome());
        assertTrue(listShip.isShipOccupied(coordinateSet1));
        assertFalse(listShip.isShipOccupied(coordinateSet3));
    }

}
