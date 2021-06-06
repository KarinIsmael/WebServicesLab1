package core;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static List<String> billboard = new ArrayList<>();

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(5050)){
            while (true){
                Socket client = serverSocket.accept();
                executorService.submit(()->handleConnection(client));
                output(client);
                //input();
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

  /*  private static void input() throws IOException {
        Socket socket = new Socket();
        InetSocketAddress socketAddress = new InetSocketAddress(5050);
        socket.connect(socketAddress);
        BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while(true){
            var line = inputFromServer.readLine();
            if(line== null || line.isEmpty()) {
                break;
            }
            System.out.println(line);
        }

        inputFromServer.close();
        socket.close();

    }*/

    private static void handleConnection(Socket client){
        System.out.println(client.getInetAddress());
        System.out.println(Thread.currentThread().getName());
    }

    private static void input(Socket client) throws IOException {
        BufferedReader inputFromClient = new BufferedReader(new InputStreamReader((client.getInputStream())));

        requestHandler(inputFromClient);
        //String line = inputFromClient.readLine();

        //System.out.println(line);

        inputFromClient.close();
        client.close();
    }

    private static void requestHandler(BufferedReader inputFromClient) throws IOException {
        List<String> templist = new ArrayList<>();

        while (true) {
            var line = inputFromClient.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }
            templist.add(line);
            System.out.println(line);
        }
        synchronized (billboard){
            billboard.addAll(templist);
        }
    }

}

