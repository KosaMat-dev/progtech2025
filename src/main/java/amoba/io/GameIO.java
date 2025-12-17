package amoba.io;

import amoba.model.Board;
import amoba.model.Player;
import java.io.*;
import java.util.Scanner;

public class GameIO {

    /**
     * Mentés metódus - Fontos: public static void és (Board, String) paraméterek!
     */
    public static void saveGame(Board board, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Első sorba mentjük a tábla méreteit
            writer.println(board.getRows() + ";" + board.getCols());

            // Végigmegyünk a táblán és elmentjük a bábuk helyét
            for (int r = 0; r < board.getRows(); r++) {
                for (int c = 0; c < board.getCols(); c++) {
                    Player p = board.getAt(r, c);
                    if (p != Player.EMPTY) {
                        writer.println(r + ";" + c + ";" + p.name());
                    }
                }
            }
        }
    }

    /**
     * Betöltés metódus - Fontos: public static Board és (String) paraméter!
     */
    public static Board loadGame(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) return null;

        try (Scanner sc = new Scanner(file)) {
            if (!sc.hasNextLine()) return null;

            String[] sizeInfo = sc.nextLine().split(";");
            int rows = Integer.parseInt(sizeInfo[0]);
            int cols = Integer.parseInt(sizeInfo[1]);

            Board board = new Board(rows, cols);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                int r = Integer.parseInt(parts[0]);
                int c = Integer.parseInt(parts[1]);
                Player p = Player.valueOf(parts[2]);

                // A betöltésnél a setForTest-et használjuk, hogy ne ütközzön szabályba
                board.setForTest(r, c, p);
            }
            return board;
        }
    }
}
