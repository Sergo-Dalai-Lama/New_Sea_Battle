import com.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LanternaInputTest {

    @ParameterizedTest
    @CsvSource({
            "set,SET",
            "unset,UNSET",
            "repos,REPOSITION",
            "'',UNKNOWN",
            "d,UNKNOWN",
            "sdgdgs,UNKNOWN"
    })
    public void inputTest(String text,ShipSetting shipSetting) throws IOException {
        Screen screen = mock(Screen.class);//создали мок-объект ввода данных
        List<KeyStroke>  keyStrokes = new ArrayList<>();
        for (char c : text.toCharArray()) {
            keyStrokes.add(new KeyStroke(c,false,false));
        }
        keyStrokes.add(new KeyStroke(KeyType.Enter));
        AtomicInteger index = new AtomicInteger(0);
        when(screen.readInput()).thenAnswer(//настройка поведения screen mock
                invocation ->
                keyStrokes.get(index.getAndIncrement())
        );
        LanternaInput inputLanterna = new LanternaInput(screen);//тестируется ввод, а не отрисовка, render создавать не надо

        assertEquals(shipSetting, inputLanterna.input());
    }

    @ParameterizedTest
    @CsvSource({
            "a1c1v,SET",
            "c3c2v,SHIP_IS_AVAILABLE",
            "k9c1v,BEYOND_FIELD",
            "'',INVALID_DATA",
            "d,INVALID_DATA",
            "sdgdgs,INVALID_DATA"
    })
    public void inputSetShipTest(String text,SetShipOutcome setShipOutcome) throws IOException {

        ListShip listShip = new ListShip();
        GameCore gameCore = new GameCore(listShip);
        gameCore.setShip(new SetShipCommand(new Coordinate(3,3),Ship.CRUISER2,Orientation.VERTICAL));
        Screen screen = mock(Screen.class);//создали мок-объект ввода данных
        List<KeyStroke>  keyStrokes = new ArrayList<>();
        for (char c : text.toCharArray()) {
            keyStrokes.add(new KeyStroke(c,false,false));
        }
        keyStrokes.add(new KeyStroke(KeyType.Enter));
        AtomicInteger index = new AtomicInteger(0);
        when(screen.readInput()).thenAnswer(
                invocation ->
                        keyStrokes.get(index.getAndIncrement())
        );
        LanternaInput inputLanterna = new LanternaInput(screen);
        SetShipCommand command1 = inputLanterna.inputSetShips();

        SetShipResult result = gameCore.setShip(command1);

        assertEquals(setShipOutcome, result.outcome());
    }

}
