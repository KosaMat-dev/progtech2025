package amoba.model;

import java.util.Arrays;

public class Board {
    private final int rows;
    private final int cols;
    private final Player[][] grid;

    public Board(int rows, int cols) {
        if (rows < 4 || rows > 25 || cols < 4 || cols > rows) {
            throw new IllegalArgumentException("Érvénytelen méret: 4 <= M <= N <= 25 szabály megsértve!");
        }
        this.rows = rows;
        this.cols = cols;
        this.grid = new Player[rows][cols];
        for (Player[] row : grid) {
            Arrays.fill(row, Player.EMPTY);
        }
    }

    public void place(int r, int c, Player p) {
        grid[r][c] = p;
    }

    public void setForTest(int r, int c, Player p) {
        if (r >= 0 && r < rows && c >= 0 && c < cols) {
            grid[r][c] = p;
        }
    }

    public boolean canPlace(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != Player.EMPTY) {
            return false;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nr = r + i;
                int nc = c + j;
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] != Player.EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkWin(Player p) {
        // Irányok: vízszintes, függőleges, két átló
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != p) continue;

                for (int[] d : directions) {
                    int count = 0;
                    for (int i = 0; i < 5; i++) {
                        int nr = r + d[0] * i;
                        int nc = c + d[1] * i;
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == p) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    if (count == 5) return true; // Megvan az 5 egy sorban!
                }
            }
        }
        return false; // Ha végigértünk és nem találtunk 5-öt
    }

    // Ezek kellenek a GameIO-nak!
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Player getAt(int r, int c) { return grid[r][c]; }
}
