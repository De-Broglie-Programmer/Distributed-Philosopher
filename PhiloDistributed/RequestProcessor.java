import java.net.*;
import java.io.*;

/*
 * the thread RequestProcessor is created by the server to process the request
 * it responses to the client
 * to get and set the fork status
 */
public class RequestProcessor extends Thread {
    private Socket socket; // Assume that you already have a socket
    private boolean[] avail_forks = { true, true, true, true, true }; // true

    public RequestProcessor(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try { // run() is not allowed to raise an exception
              // Buffered character input stream
            InputStream is = socket.getInputStream();
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader rd = new BufferedReader(ir);
            // Buffered character output stream
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(os);
            BufferedWriter wr = new BufferedWriter(ow);
            // Then:
            String line;
            while (!socket.isClosed()) {
                line = rd.readLine();
                if (line == null)
                    continue;
                // read the request from the client
                String[] request = line.split(" ");
                if (request[0].equals("request")) {
                    // check if the fork is available
                    if (avail_forks[Integer.parseInt(request[2])] == true) {
                        // send reply to the client
                        wr.write("reply: fork " + request[2] + "is available\n");
                        wr.flush();
                    } else {
                        // send reply to the client
                        wr.write("reply: fork " + request[2] + "is unavailable\n");
                        wr.flush();
                    }
                } else if (request[0].equals("set")) {
                    if (request[4].equals("unavailable")) {
                        // set the fork to unavailable
                        avail_forks[Integer.parseInt(request[2])] = false;
                        avail_forks[Integer.parseInt(request[3])] = false;
                        // send reply to the client
                        wr.write("ok\n");
                        wr.flush();
                    } else if (request[4].equals("available")) {
                        // set the fork to available
                        avail_forks[Integer.parseInt(request[2])] = true;
                        avail_forks[Integer.parseInt(request[3])] = true;
                        // send reply to the client
                        wr.write("ok\n");
                        wr.flush();
                    }
                } else if (request[0].equals("quit")) {
                    socket.close();
                    System.out.println("socket closed!");
                }
            }
            wr.flush();
            // close the streams
            rd.close();
            wr.close();
            System.out.println("Server thread terminates!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
