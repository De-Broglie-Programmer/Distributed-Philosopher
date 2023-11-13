import java.net.ServerSocket;
import java.net.Socket;

/*
 * run this class to start the server
 * the server runs continuously and listens to port 40080 for connection from the client
 * you can start server first, then start client to connect to the server
 * the server creates a thread to process the request from the client
 */
public class ServeurTable {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;
        System.out.println("begin!");
        // create a server socket
        // wait for connection continuously

        try {
            serverSocket = new ServerSocket(40080);
            socket = serverSocket.accept();
            System.out.println("Connection Success!");
            RequestProcessor rp = new RequestProcessor(socket);
            rp.start();
            try {
                rp.join();
            } catch (Exception e) {
                System.out.println("Interrupted in joining thread!");
                e.printStackTrace();
            }
            // close the server socket
            serverSocket.close();
            System.out.println("ServerSocket 40080 closed!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
