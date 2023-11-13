/*
 * Table.java
 * implement the allocate() and release() methods by condition variables
 * the condition variable is implemented by wait() and notifyAll()
 * which are protected by synchronized
 */

public class Table {
    int numPhilo; // numPhilo >= 2
    private boolean[] avail_forks = { true, true, true, true, true }; // true means available

    public Table(int numPhilo) {
        this.numPhilo = numPhilo;
    }

    public synchronized void allocate(int philoNo) {
        int lforkNo = philoNo;
        int rforkNo = (philoNo + 1) % numPhilo;
        System.out.println("Philo " + philoNo + " attemps eating");
        try {
            while (true) {
                if (avail_forks[lforkNo] == false || avail_forks[rforkNo] == false) {
                    wait(); // no need to catch InterruptedException, since it will
                            // re-check the condition again after being notified or interrupted
                } else {
                    avail_forks[lforkNo] = false;
                    avail_forks[rforkNo] = false;
                    System.out.println("Philo " + philoNo + " begins eating");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void release(int philoNo) {
        int lforkNo = philoNo;
        int rforkNo = (philoNo + 1) % numPhilo;
        avail_forks[lforkNo] = true;
        avail_forks[rforkNo] = true;
        notifyAll();
        System.out.println("Philo " + philoNo + " finishes eating");
    }

    public int getNumPhilo() {
        return numPhilo;
    }

    //
}
