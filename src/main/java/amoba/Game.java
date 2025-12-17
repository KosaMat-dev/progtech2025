package amoba;

import amoba.model.*;
import amoba.io.GameIO;
import amoba.db.DatabaseManager;
import java.util.*;
import java.io.IOException;

public class Game {
    private Board board;
    private final Scanner sc = new Scanner(System.in);
    private String playerName;

    public void start() {
        while (true) {
            System.out.println("\n--- AMOBA MENU ---");
            System.out.println("1. Új játék | 2. Betöltés | 3. Ranglista | 4. Kilépés");
            String c = sc.nextLine();
            if (c.equals("1")) initNewGame();
            else if (c.equals("2")) loadGame();
            else if (c.equals("3")) DatabaseManager.printHighScores();
            else if (c.equals("4")) System.exit(0);
        }
    }

    private void initNewGame() {
        System.out.print("Neved: ");
        playerName = sc.nextLine();

        int n, m;
        while (true) {
            try {
                System.out.print("Sorok száma (N, 4-25): ");
                n = Integer.parseInt(sc.nextLine());
                System.out.print("Oszlopok száma (M, 4-N): ");
                m = Integer.parseInt(sc.nextLine());

                if (n >= 4 && n <= 25 && m >= 4 && m <= n) break;
                else System.out.println("Szabályszegés! (4 <= M <= N <= 25)");
            } catch (Exception e) {
                System.out.println("Hibás formátum!");
            }
        }

        board = new Board(n, m);
        board.place((n - 1) / 2, (m - 1) / 2, Player.O);
        gameLoop();
    }

    private void loadGame() {
        try {
            board = GameIO.loadGame("mentes.txt");
            if (board != null) {
                System.out.println("Játék betöltve.");
                gameLoop();
            }
        } catch (IOException e) {
            System.out.println("Nincs mentett játék.");
        }
    }

    private void gameLoop() {
        while (true) {
            printBoard();
            if (humanMove()) return;
            if (board.checkWin(Player.X)) {
                printBoard();
                System.out.println("GRATULÁLOK, " + playerName + "! Nyertél!");
                DatabaseManager.saveWin(playerName);
                return;
            }
            aiMove();
            if (board.checkWin(Player.O)) {
                printBoard();
                System.out.println("Sajnos a gép nyert!");
                return;
            }
        }
    }

    private boolean humanMove() {
        while (true) {
            System.out.print("Lépés (pl. a1) vagy 'exit': ");
            String in = sc.nextLine().toLowerCase().trim();
            if (in.equals("exit")) {
                try {
                    GameIO.saveGame(board, "mentes.txt");
                    System.out.println("Játék elmentve.");
                } catch(IOException e) {
                    System.out.println("Mentési hiba.");
                }
                return true;
            }
            try {
                int c = in.charAt(0) - 'a';
                int r = Integer.parseInt(in.substring(1)) - 1;
                if (board.canPlace(r, c)) {
                    board.place(r, c, Player.X);
                    return false;
                }
            } catch (Exception e) {}
            System.out.println("Ide nem léphetsz! (Csak szomszédos szabad mezőre)");
        }
    }

    private void aiMove() {
        Random rnd = new Random();
        while (true) {
            int r = rnd.nextInt(board.getRows());
            int c = rnd.nextInt(board.getCols());
            if (board.canPlace(r, c)) {
                board.place(r, c, Player.O);
                break;
            }
        }
    }

    private void printBoard() {
        System.out.print("\n   ");
        for (int j = 0; j < board.getCols(); j++) System.out.print((char)('a' + j) + " ");
        System.out.println();
        for (int i = 0; i < board.getRows(); i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < board.getCols(); j++) {
                Player p = board.getAt(i, j);
                System.out.print((p == Player.X ? "X " : p == Player.O ? "O " : ". "));
            }
            System.out.println();
        }
    }
}
