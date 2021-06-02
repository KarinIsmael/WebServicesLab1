package core;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(5050)){
            while (true){
                Socket client = serverSocket.accept();
                System.out.println(client.getInetAddress());
                PrintWriter outputToClient = new PrintWriter(client.getOutputStream());
                outputToClient.print("HTTP/1.1 404 Not Found\r\nContent-length: 0\r\n\r\n");

                outputToClient.flush();
                outputToClient.close();
                client.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
