import java.util.Random;
import java.util.concurrent.Semaphore;

public class Player implements Runnable {
    private final Semaphore myTurn;
    private final Semaphore opponentTurn;
    private static boolean gameOver = false;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private final String[] figures = {"пішаком", "конем", "турою", "ферзем", "слоном", "королем"};
    private final String name;
    private final String opponentName;
    private int moveCount = 0;

    public Player(String name, String opponentName, Semaphore myTurn, Semaphore opponentTurn) {
        this.name = name;
        this.opponentName = opponentName;
        this.myTurn = myTurn;
        this.opponentTurn = opponentTurn;
    }

    @Override
    public void run() {

        while (!gameOver) {
            try {
                myTurn.acquire();  // Очікування своєї черги на хід
                if (gameOver) break;

                makeMove();

                // Перевірка умов завершення гри
                if (checkGameOver() && moveCount >= 2) {
                    gameOver = true;
                    opponentTurn.release();  // Вивільняємо семафор, щоб інший потік міг завершити гру
                    System.out.println(ANSI_GREEN + "Мат! Гравець " + name + " не залишив ходів для " + opponentName);
                    System.out.println("Вітаємо з перемогою " + name + "\nГру завершено!" + ANSI_RESET);
                    break;
                }

                opponentTurn.release();  // Передаємо чергу опоненту
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void makeMove() {
        try {
            Random random = new Random();
            int randomIndex;
            String figure;

            moveCount++;

            if (moveCount <= 2)
                randomIndex = random.nextInt(2);
            else
                randomIndex = random.nextInt(figures.length);

            figure = figures[randomIndex];

            System.out.println("Гравець " + ANSI_PURPLE + name + ANSI_RESET + " зробив хід " + ANSI_BLUE + figure + ANSI_RESET);
            Thread.sleep(500);

            System.out.println("Гравець " + ANSI_PURPLE + name + ANSI_RESET + " очікує хід від опонента.\n");
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkGameOver() {
        return Math.random() < 0.1;
    }
}
