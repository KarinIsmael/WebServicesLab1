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

public class DatabaseManagement {
    static void saveMessage(String url, OutputStream outputToClient) throws IOException {

            EntityManager em = Main.emf.createEntityManager();
            String line = url;

            System.out.println("test");
//                if (line.startsWith("GET") && line.contains("/sendmessage")) {
                if (line.contains("/sendmessage/")) {

                    String message = line.substring(13);

                    Usermessage usermessage = new Usermessage(message);

                    em.getTransaction().begin();
                    em.persist(usermessage);
                    em.getTransaction().commit();

                    Gson gson = new Gson();

                    String text = "Following message has been saved to the server: ";

                    String json = gson.toJson(text + message);

                    System.out.println(json);

                    byte[] data = json.getBytes(StandardCharsets.UTF_8);
                    String header = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-length: " + data.length + "\r\n\r\n";
                    outputToClient.write(header.getBytes());
                    outputToClient.write(data);
                    outputToClient.flush();
                    //outputToClient.close();
                    //Main.emf.close();
                    em.close();
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

    static void saveIpToDatabase(Socket client, OutputStream outputToClient) throws IOException {

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

             WelcomeMessages.welcomeNewUser();

         } else if(ipAdresses.contains(userIp))
        {

            //OutputStream outputToClient = client.getOutputStream();

            String message = WelcomeMessages.welcomeOldUser();

            Main.getGson(outputToClient, message);
            System.out.println(userIp+" visited again!");

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
            //System.out.println(userIp+" visited again!");

            //PrintWriter outputToClient = new PrintWriter(client.getOutputStream());
            //outputToClient.print("HTTP/1.1 200 OK \r\nThank you for visiting again!");
        }
        em.close();
    }

}
