package core;

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

import static core.Main.getGson;

public class DatabaseManagement {
static String message;
    public static void saveMessage(String url, OutputStream outputToClient) throws IOException {

            EntityManager em = Main.emf.createEntityManager();
            String line = url;

                if (line.contains("/sendmessage/")) {

                    String msg = line.substring(13);

                    Usermessage usermessage = new Usermessage(msg);

                    em.getTransaction().begin();
                    em.persist(usermessage);
                    em.getTransaction().commit();

                    getGson(outputToClient, message);
                    em.close();
                }

            }

    static void sendIpAdresses(OutputStream outputToClient) throws IOException {

        EntityManager em = Main.emf.createEntityManager();
        TypedQuery<String> query = em.createQuery("SELECT ip.ipAddress FROM UserInfo ip", String.class);

        List<String> ipAdresses = query.getResultList();

        getGson(outputToClient,message);

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

            getGson(outputToClient, message);
            System.out.println(userIp+" visited again!");
        }
        em.close();
    }

}
