
import java.util.*;

/*
 * ThreadPhilo.java
 * each thread is a philo
 * the thread attempts eating for about 15 seconds
 */
public class ThreadPhilo extends Thread {
    private int philoNo = 0;
    RequestTable table;

    public ThreadPhilo(int philoNo, RequestTable t) {
        this.philoNo = philoNo;
        table = t;
    }

    @Override
    public void run() {
        int n = 0;
        // stop the while loop after 15*1000 ms
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 15 * 1000) {
            table.allocate(philoNo);
            Random r = new Random();
            try {
                Thread.sleep(r.nextInt(256));
            } catch (Exception e) {
                e.printStackTrace();
            }
            table.release(philoNo);
            n++;
            System.out.println("Philo " + philoNo + " has eaten " + n + " times");
            try {
                Thread.sleep(r.nextInt(1024));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // wait for 1 second to print the total number of eating times
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("In total, Philo " + philoNo + " has eaten " + n + " times");
    }
}
