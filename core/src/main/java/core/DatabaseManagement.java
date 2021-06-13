package core;

import com.google.gson.Gson;
import user.UserInfo;
import user.Usermessage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static core.Main.*;

public class DatabaseManagement {
    static String message;

    public static void saveMessage(String url, OutputStream outputToClient) throws IOException {

        EntityManager em = Main.emf.createEntityManager();
        String line = url;

        if (line.contains("/sendmessage/")) {

            String message = line.substring(13);

            Usermessage usermessage = new Usermessage(message);

            em.getTransaction().begin();
            em.persist(usermessage);
            em.getTransaction().commit();

            getGson(outputToClient, message);
            em.close();
        }

    }

    public static void sendIpAdresses(OutputStream outputToClient) throws IOException {

        EntityManager em = Main.emf.createEntityManager();
        TypedQuery<String> query = em.createQuery("SELECT ip.ipAddress FROM UserInfo ip", String.class);

        List<String> ipAdresses = query.getResultList();

        getGson(outputToClient, ipAdresses);

    }

    public static void saveIpToDatabase(Socket client, OutputStream outputToClient) throws IOException {

        InetAddress ipAdress = client.getInetAddress();
        String userIp = ipAdress.toString();

        EntityManager em = emf.createEntityManager();

        TypedQuery<String> query = em.createQuery("SELECT ip.ipAddress FROM UserInfo ip", String.class);

        List<String> ipAdresses = query.getResultList();

        if (!ipAdresses.contains(userIp)) {
            UserInfo user = new UserInfo(userIp);

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

        } else if (ipAdresses.contains(userIp)) {
            System.out.println(userIp + " visited again!");
        }
        em.close();
    }

    public static void showMessages(OutputStream outputToClient) throws IOException {

        EntityManager em = emf.createEntityManager();
        TypedQuery<String> query = em.createQuery("SELECT messages.message FROM Usermessage messages", String.class);
        List<String> usermessages = query.getResultList();
        getGson(outputToClient, usermessages);
    }

    public static void serviceloaderMessage(OutputStream outputToClient, Socket client) throws IOException {

        EntityManager em = emf.createEntityManager();

        InetAddress ipAdress = client.getInetAddress();
        String userIp = ipAdress.toString();
        TypedQuery<String> query = em.createQuery("SELECT ip.ipAddress FROM UserInfo ip", String.class);
        List<String> ipAdresses = query.getResultList();
        if (!ipAdresses.contains(userIp)) {
            String header = "";
            String message = WelcomeMessages.welcomeNewUser();
            Gson gson = new Gson();
            String json = gson.toJson(message);
            byte[] data = json.getBytes(StandardCharsets.UTF_8);
            header = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-length: " + data.length + "\r\n\r\n";
            outputToClient.write(header.getBytes());
            outputToClient.write(data);
            outputToClient.flush();
        } else if (ipAdresses.contains(userIp)) {
            String header = "";
            String message = WelcomeMessages.welcomeOldUser();
            Gson gson = new Gson();
            String json = gson.toJson(message);
            byte[] data = json.getBytes(StandardCharsets.UTF_8);
            header = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-length: " + data.length + "\r\n\r\n";
            outputToClient.write(header.getBytes());
            outputToClient.write(data);
            outputToClient.flush();
        }
        em.close();
    }
}
