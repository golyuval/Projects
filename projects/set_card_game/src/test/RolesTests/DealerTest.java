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

@ExtendWith(MockitoExtension.class)
class DealerTest {


    Player[] players;
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
        assertTrue(players.length >= 0);
    }

    @BeforeEach
    void setUp() {
        // purposely do not find the configuration files (use defaults here).
        Env env = new Env(logger, new Config(logger, (String) null), ui, util);
        players = new Player[2];
        players[0] = new Player(env, dealer, table, 0, true);
        players[1] = new Player(env, dealer, table, 0, true);
        dealer = new Dealer(env,table,players);
        assertInvariants();
    }

    @AfterEach
    void tearDown() {
        assertInvariants();
    }

    @Test
    void validation_check(){
        players[0].getTokens().clear();
        players[0].getTokens().add(1);
        players[0].getTokens().add(2);
        boolean NotValid = false;
        assertEquals(dealer.validation_check(players[0]),NotValid);
    }

    @Test
    void resetTimer(){
        dealer.displayTimer = 0;
        dealer.resetTimer();
        long expectedTime = dealer.displayTimerFinal;
        assertEquals(dealer.displayTimer,expectedTime);

    }

}