import java.util.*;

/* 
 *The main class for the dining philosophers problem
 *The main class creates a table and a number of philosophers
 *The philosophers are started in a random order
 *The main class waits for the philosophers to finish by calling join()
 *The program runs for about 16 seconds
*/
public class MainPhilo {

    public static void main(String[] args) {
        int numberOfPhilos = 5;
        Table table = new Table(numberOfPhilos);

        ArrayList<ThreadPhilo> threads = new ArrayList<>();
        for (int i = 0; i < numberOfPhilos; i++) {
            threads.add(new ThreadPhilo(i, table));
        }

        // get a random order of philos attempting eating
        ArrayList<Integer> range = new ArrayList<>();
        for (int i = 0; i < numberOfPhilos; i++) {
            range.add(i);
        }
        Random random = new Random();
        for (int i = 0; i < numberOfPhilos; i++) {
            int index = random.nextInt(range.size());
            threads.get(range.get(index)).start(); // philos attempts eating
            range.remove(index);
        }

        for (int i = 0; i < numberOfPhilos; i++) {
            try {
                threads.get(i).join();
            } catch (Exception e) {
            }
        }
    }
}
