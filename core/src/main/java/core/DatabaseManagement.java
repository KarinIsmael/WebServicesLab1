package core;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import interf.TypeOfUser;
import interf.Welcome;
import user.UserInfo;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ServiceLoader;

public class DatabaseManagement {
    static void saveMessage(BufferedReader inputFromClient, OutputStream outputToClient) throws IOException {

            EntityManager em = Main.emf.createEntityManager();

        String line = inputFromClient.readLine();

            while(true) {
//                if (line.startsWith("GET") && line.contains("/sendmessage")) {
                if (line.contains("/sendmessage/")) {
                    String message = line.split("/sendmessage/")[15];

                    UserInfo userInfo = new UserInfo(message);

                    em.getTransaction().begin();
                    em.persist(userInfo);
                    em.getTransaction().commit();

                    Gson gson = new Gson();

                    String json = gson.toJson(message);
                    System.out.println("Following message has been saved to the server: " + json);

                    byte[] data = json.getBytes(StandardCharsets.UTF_8);
                    String header = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-length: " + data.length + "\r\n\r\n";
                    outputToClient.write(header.getBytes());
                    outputToClient.write(data);
                    outputToClient.flush();
                    Main.emf.close();
                    em.close();
                }

                }
            }

    static void sendIpAdresses(OutputStream outputToClient) throws IOException {

        EntityManager em = Main.emf.createEntityManager();

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

    static void saveIpToDatabase(Socket client) throws IOException {

        //ServiceLoader<Welcome> welcomes = ServiceLoader.load(Welcome.class);

        InetAddress ipAdress = client.getInetAddress();
        String userIp = ipAdress.toString();

        EntityManager em = Main.emf.createEntityManager();

        TypedQuery<String> query = em.createQuery("SELECT ip.ipAddress FROM UserInfo ip", String.class);

        List<String> ipAdresses = query.getResultList();

        if(!ipAdresses.contains(userIp))
         {
            UserInfo user = new UserInfo(userIp);

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

             WelcomeMessages.WelcomeNewUser();

         } else if(ipAdresses.contains(userIp))
        {
           /* Welcome welcome = null;
            TypeOfUser annotation = welcome.getClass().getAnnotation(TypeOfUser.class);
            annotation.equals("/old");
            String hej = WelcomeMessages.welcomeOldUser();
            Gson gson = new Gson();
            String json = gson.toJson(hej);
            System.out.println(json);

            byte[] data = json.getBytes(StandardCharsets.UTF_8);

            String header = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-length: " + data.length+"\r\n\r\n";
            outputToClient.write(header.getBytes());
            outputToClient.write(data);
            outputToClient.flush();*/
            System.out.println(userIp+" visited again!");

            //PrintWriter outputToClient = new PrintWriter(client.getOutputStream());
            //outputToClient.print("HTTP/1.1 200 OK \r\nThank you for visiting again!");
        }
        em.close();
    }

}
