
import java.util.*;
import java.net.*;
import java.io.*;

/*
 *run this class to start the client
 *there are 5 philos, each philo is a thread
 *the threads are started in a random order
 *the client runs for about 16 seconds
*/

public class ClientPhilo {

    public static void main(String[] args) {
        String serverName = "localhost";
        int port = 40080;
        try {
            InetAddress addr = InetAddress.getByName(serverName);
            Socket client = new Socket(addr, port);
            System.out.println("begin client!");
            // Buffered character input stream
            InputStream is = client.getInputStream();
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader rd = new BufferedReader(ir);
            // Buffered character output stream
            OutputStream os = client.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(os);
            BufferedWriter wr = new BufferedWriter(ow);

            int numberOfPhilos = 5;
            RequestTable table = new RequestTable(numberOfPhilos, wr, rd);

            // each thread is a philo
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
                    System.out.println("interrupted");
                    e.printStackTrace();
                }
            }

            wr.write("quit");
            wr.flush();
            // closing socket will close all streams associated with it
            if (client != null) {
                client.close();
            }
            System.out.println("end client!");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
