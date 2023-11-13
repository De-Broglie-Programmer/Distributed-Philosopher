import java.io.*;

/*
 * RequestTable.java
 * implement the allocate() and release() methods by condition variables
 * the condition variable is implemented by wait() and notifyAll()
 * which are protected by synchronized
 * the request table is on the client side
 * the client sends request to the server to get and set the fork status
 */
public class RequestTable {
    int numPhilo; // numPhilo >= 2
    BufferedWriter wr;
    BufferedReader rd;

    public RequestTable(int numPhilo, BufferedWriter wr, BufferedReader rd) {
        this.numPhilo = numPhilo;
        this.wr = wr;
        this.rd = rd;
    }

    public synchronized void allocate(int philoNo) {
        int lforkNo = philoNo;
        int rforkNo = (philoNo + 1) % numPhilo;
        boolean avail_left = false;
        boolean avail_right = false;
        System.out.println("Philo " + philoNo + " attemps eating");
        try {
            while (true) {
                // request the forks of the left and right from the server
                wr.write("request fork " + lforkNo + "\n");
                wr.flush();
                wr.write("request fork " + rforkNo + "\n");
                wr.flush();

                // read the reply from the server
                String reply = null;
                while ((reply = rd.readLine()) == null) {
                    // wait until receive reply from server
                    Thread.sleep(100);
                }
                if (reply.equals("reply: fork " + lforkNo + "is available")) {
                    avail_left = true;
                } else if (reply.equals("reply: fork " + lforkNo + "is unavailable")) {
                    avail_left = false;
                }
                while ((reply = rd.readLine()) == null) {
                    // wait until receive reply from server
                    Thread.sleep(100);
                }
                if (reply.equals("reply: fork " + rforkNo + "is available")) {
                    avail_right = true;
                } else if (reply.equals("reply: fork " + rforkNo + "is unavailable")) {
                    avail_right = false;
                }

                // if either fork is unavailable, wait
                if (avail_left == false || avail_right == false) {
                    wait(); // no need to catch InterruptedException, since it will
                            // check the condition again
                } else {
                    // request to set the forks of the left and right to unavailable on the server
                    wr.write("set fork " + lforkNo + " " + rforkNo + " unavailable\n");
                    wr.flush();
                    // wait until receive ok from server
                    while ((reply = rd.readLine()) == null) {
                        Thread.sleep(100);
                    }
                    if (reply.equals("ok")) {
                        System.out.println("Philo " + philoNo + " begins eating");
                        return;
                    }
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
        // request to set the forks of the left and right to available on the server
        try {
            wr.write("set fork " + lforkNo + " " + rforkNo + " available\n");
            wr.flush();

            // wait until receive ok from server
            String reply = null;
            while ((reply = rd.readLine()) == null) {
                Thread.sleep(100);
            }
            if (reply.equals("ok")) {
                System.out.println("Philo " + philoNo + " finishes eating");
            }
        } catch (Exception e) {
            System.out.println("write failed");
            e.printStackTrace();
        }
        notifyAll();
    }

    public int getNumPhilo() {
        return numPhilo;
    }

}
