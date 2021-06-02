package core;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(5050)){
            while (true){
                Socket client = serverSocket.accept();
                executorService.submit(()->handleConnection(client));
                output(client);

                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void output(Socket client) throws IOException {

        PrintWriter outputToClient = new PrintWriter(client.getOutputStream());
        outputToClient.print("HTTP/1.1 404 Not Found\r\nContent-length: 0\r\n\r\n");

        outputToClient.flush();
        outputToClient.close();

    }

    private static void handleConnection(Socket client) {
        System.out.println(client.getInetAddress());
        System.out.println(Thread.currentThread().getName());
    }
}
