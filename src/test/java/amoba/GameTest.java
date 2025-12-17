package amoba;

import amoba.db.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

public class GameTest {

    @Test
    public void testSaveWinWithMock() {
        // Mivel a saveWin static, a mockStatic-ot kell használnunk
        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {

            String winner = "Teszt Elek";

            // 1. Meghívjuk a te saját metódusodat
            DatabaseManager.saveWin(winner);

            // 2. Ellenőrizzük a Mockito-val, hogy lefutott-e a statikus hívás
            mockedDb.verify(
                    () -> DatabaseManager.saveWin(winner),
                    times(1)
            );
        }
    }
}