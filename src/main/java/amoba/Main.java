package amoba;

/**
 * A program fő belépési pontja.
 */
public class Main {

    public static void main(String[] args) {
        // Létrehozzuk a Game osztály egy példányát.
        Game game = new Game();

        // Elindítjuk a játék ciklusát.
        game.start();
    }
}
