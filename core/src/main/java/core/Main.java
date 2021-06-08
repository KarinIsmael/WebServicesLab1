package core;

import com.google.gson.Gson;
import user.UserInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static List<String> billboard = new ArrayList<>();

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("user");

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(5050)){
            while (true){
                Socket client = serverSocket.accept();
                executorService.submit(()->handleConnection(client));
                //flytta allt code till annan method efter handleconnection

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sendIpAdresses(OutputStream outputToClient) throws IOException {
        EntityManager em = emf.createEntityManager();

        TypedQuery<String> query = em.createQuery("SELECT ip.ipAddress FROM UserInfo ip", String.class);

        List<String> ipAdresses = query.getResultList();

        Gson gson = new Gson();
        String json = gson.toJson(ipAdresses);
        System.out.println(json);

        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        String header = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-length: " + data.length+"\r\n\r\n";
        outputToClient.write(header.getBytes());
        outputToClient.write(data);
        outputToClient.flush();
    }


    private static void saveIpToDatabase(Socket client) throws IOException {
        InetAddress ipAdress = client.getInetAddress();
        String userIp = ipAdress.toString();

        EntityManager em = emf.createEntityManager();

        TypedQuery<String> query = em.createQuery("SELECT ip.ipAddress FROM UserInfo ip", String.class);

        List<String> ipAdresses = query.getResultList();

        if(!ipAdresses.contains(userIp))
         {
            UserInfo user = new UserInfo(userIp);

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

        } else if(ipAdresses.contains(userIp))
        {
            System.out.println(userIp+" visited again!");

            //PrintWriter outputToClient = new PrintWriter(client.getOutputStream());
            //outputToClient.print("HTTP/1.1 200 OK \r\nThank you for visiting again!");
        }
        em.close();
    }

    private static void sendResponse(OutputStream outputToClient) throws IOException {

        String response = "Thank you for visiting again";

        Gson gson = new Gson();
        String json = gson.toJson(response);
        System.out.println(json);

        byte[] data = json.getBytes(StandardCharsets.UTF_8);

        String header = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-length: " + data.length+"\r\n\r\n";
        outputToClient.write(header.getBytes());
        outputToClient.write(data);
        outputToClient.flush();
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

    private static String requestHandler(BufferedReader inputFromClient) throws IOException {
        var url="";

        List<String> templist = new ArrayList<>();

        while (true) {
            var line = inputFromClient.readLine();
            if (line.startsWith("GET")) {
                url = line.split(" ")[1];
            } else if (line.startsWith("POST")) {
                url = line.split(" ")[1];
            } else if (line.startsWith("HEAD")) {
                url = line.split(" ")[1];
            } else if (line == null || line.isEmpty()) {
                break;
            }
            templist.add(line);
            System.out.println(line);
        }
        synchronized (billboard){
            billboard.addAll(templist);
        }
        return url;
    }

}

