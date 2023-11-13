import java.util.*;

/*
 * ThreadPhilo.java
 * implement the run() method
 * the philo attempts eating during 15*1000 ms
 */
public class ThreadPhilo extends Thread {
    private int philoNo = 0;
    Table table;

    public ThreadPhilo(int philoNo, Table t) {
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
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("In total, Philo " + philoNo + " has eaten " + n + " times");
    }
}
