package amoba.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        // Minden teszt előtt egy tiszta 10x10-es táblát hozunk létre
        board = new Board(10, 10);
    }

    @Test
    void testInitialization() {
        assertEquals(10, board.getRows());
        assertEquals(10, board.getCols());
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                assertEquals(Player.EMPTY, board.getAt(r, c));
            }
        }
    }

    @Test
    void testCanPlaceRules() {
        // Manuálisan leteszünk egyet a teszthez
        board.setForTest(5, 5, Player.X);

        // Szabályos: szomszédos (vízszintes, függőleges, átlós)
        assertTrue(board.canPlace(5, 6), "Vízszintes szomszédnak működnie kell");
        assertTrue(board.canPlace(4, 4), "Átlós szomszédnak működnie kell");

        // Szabálytalan: túl messze van
        assertFalse(board.canPlace(0, 0), "Túl távoli mezőre nem szabad lépni");

        // Szabálytalan: már foglalt
        assertFalse(board.canPlace(5, 5), "Foglalt mezőre nem szabad lépni");

        // Szabálytalan: pályán kívül
        assertFalse(board.canPlace(-1, 5), "Pályán kívülre nem szabad lépni");
    }

    @Test
    void testWinConditions() {
        // Vízszintes győzelem teszt
        for (int i = 0; i < 5; i++) board.setForTest(2, i, Player.X);
        assertTrue(board.checkWin(Player.X), "Vízszintes 5-öst fel kell ismerni");

        // Függőleges győzelem teszt
        board = new Board(10, 10); // Tiszta tábla
        for (int i = 0; i < 5; i++) board.setForTest(i, 3, Player.O);
        assertTrue(board.checkWin(Player.O), "Függőleges 5-öst fel kell ismerni");

        // Átlós győzelem teszt
        board = new Board(10, 10);
        for (int i = 0; i < 5; i++) board.setForTest(i, i, Player.X);
        assertTrue(board.checkWin(Player.X), "Átlós 5-öst fel kell ismerni");
    }

    @Test
    void testNoWinYet() {
        // Csak 4 van egymás mellett - nem szabad nyernie
        for (int i = 0; i < 4; i++) board.setForTest(0, i, Player.X);
        assertFalse(board.checkWin(Player.X), "4 bábu még nem győzelem");
    }
}
