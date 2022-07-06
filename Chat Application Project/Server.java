import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;;

class Server {

    // Server's socket object
    ServerSocket server_socket;
    // Client's socket object
    Socket client_socket;

    BufferedReader br;
    PrintWriter pw;

    // Creating Server constructor
    public Server() throws IOException {
        // declare port number to establish specific connection
        server_socket = new ServerSocket(7777);
        System.out.println("Server is ready to connect");
        System.out.println("Waiting to connect");
        // accept client's connection
        client_socket = server_socket.accept();
        br = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
        pw = new PrintWriter(client_socket.getOutputStream());

        readMessages();
        writeMessages();

    }

    private void writeMessages() {
        // thread for writing messages and sending
        Runnable write = () -> {
            System.out.println("Starting Message Writer");
            try {
                while (!client_socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader((System.in)));
                    String msg_content = br1.readLine();
                    pw.println(msg_content);
                    pw.flush();
                    if (msg_content.equals("exit")) {
                        client_socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };
        // start the thread
        new Thread(write).start();
    }

    private void readMessages() {
        // thread for reading messages

        Runnable read = () -> {
            System.out.println("Starting Message Reader");

            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Chat is terminated by Client , Bye !");
                        client_socket.close();
                        break;
                    }
                    System.out.println("Client : " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };
        // start the thread
        new Thread(read).start();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting Server's chat Window");
        // create server object
        new Server();
    }
}