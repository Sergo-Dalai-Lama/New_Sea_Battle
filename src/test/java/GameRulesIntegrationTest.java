import com.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameRulesIntegrationTest {

    @Test
    @Timeout(1)
    public void testGameRulesIntegration_twoBotsPlay() {
        Bot bot1 = new Bot();
        Bot bot2 = new Bot();
        GameRules gameRules1 = bot1.getGameRules();
        GameRules gameRules2 = bot2.getGameRules();
        List<AttackResult> lastResults1 = new ArrayList<>();
        List<AttackResult> lastResults2 = new ArrayList<>();
        while (true) {
            lastResults1 = bot1.start(gameRules2);
            if (bot1.getState() == GameState.WIN) {
                break;
            }

            lastResults2 = bot2.start(gameRules1);
            if (bot2.getState() == GameState.WIN) {
                break;
            }
        }
        //а здесь использую ==, по факту XNOR
        assertEquals((GameState.WIN == bot1.getState()), (AttackOutcome.GAME_FINISHED == lastResults1.getLast().outcome()));
        assertEquals((GameState.WIN == bot2.getState()), (AttackOutcome.GAME_FINISHED == lastResults2.getLast().outcome()));

        assertFalse(lastResults1.isEmpty(), "Бот 1 не выполнил ни одной атаки");
        assertFalse(lastResults2.isEmpty(), "Бот 2 не выполнил ни одной атаки");
    }
}
