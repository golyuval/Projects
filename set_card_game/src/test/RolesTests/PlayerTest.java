import Admin.Config;
import Admin.Env;
import Roles.Dealer;
import Roles.Player;
import Roles.Table;
import User.UserInterface;
import Util.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    Player player;
    @Mock
    Util util;
    @Mock
    private UserInterface ui;
    @Mock
    private Table table;
    @Mock
    private Dealer dealer;
    @Mock
    private Logger logger;

    void assertInvariants() {
        assertTrue(player.id >= 0);
        assertTrue(player.score() >= 0);
    }

    @BeforeEach
    void setUp() {
        // purposely do not find the configuration files (use defaults here).
        Env env = new Env(logger, new Config(logger, (String) null), ui, util);
        player = new Player(env, dealer, table, 0, false);
        assertInvariants();
    }

    @AfterEach
    void tearDown() {
        assertInvariants();
    }

    @Test
    void point() {

        // force table.countCards to return 3
        when(table.countCards()).thenReturn(3); // this part is just for demonstration

        // calculate the expected score for later
        int expectedScore = player.score() + 1;

        // call the method we are testing
        player.point();

        // check that the score was increased correctly
        assertEquals(expectedScore, player.score());

        // check that ui.setScore was called with the player's id and the correct score
        verify(ui).setScore(eq(player.id), eq(expectedScore));
    }

    //@Test
    // void tokensToCards() {
    //     player.getTokens().clear();
    //     player.getTokens().add(1);
    //     player.getTokens().add(2);
    //     player.getTokens().add(3);
    //     player.table.slotToCard[1] = 1;
    //     player.table.slotToCard[2] = 2;
    //     player.table.slotToCard[3] = 3;
    //     int[] cards = {1,2,3};
    //     assertEquals(cards, player.tokensToCards());
    // }

    @Test
    void tokenExists() {
        int tokenSlot = 1;
        player.getTokens().clear();
        player.getTokens().add(1);
        boolean expectedExist = true;
        assertEquals(expectedExist, player.tokenExists(tokenSlot));
    }

    @Test
    void removeT() {
        int tokenSlot = 1;
        player.getTokens().add(1); // adding 1 to player tokens.
        player.removeT(tokenSlot);
        boolean expectedEmpty = true;
        assertEquals(expectedEmpty, player.getTokens().isEmpty());
    }
}