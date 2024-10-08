import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {
    private static final Semaphore player1Turn = new Semaphore(1);
    private static final Semaphore player2Turn = new Semaphore(0);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Введіть ім'я 1-го гравця: ");
        String player1Name = sc.nextLine();

        System.out.print("Введіть ім'я 2-го гравця: ");
        String player2Name = sc.nextLine();

        System.out.println("\nПочаток гри\n");

        Thread player1 = new Thread(new Player(player1Name, player2Name, player1Turn, player2Turn));
        Thread player2 = new Thread(new Player(player2Name, player1Name, player2Turn, player1Turn));

        player1.start();
        player2.start();

        try {
            player1.join();
            player2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
