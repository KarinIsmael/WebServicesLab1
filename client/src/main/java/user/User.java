package user;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class User {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("user");

    /*public static void saveIpAdress(){
        try {
            Socket socket = new Socket("192.168.1.155", 5050);

            InetAddress ipAdress = socket.getInetAddress();
            EntityManager em = emf.createEntityManager();

            UserInfo user = new UserInfo(ipAdress);

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            em.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    }

    /*public static void main(String[] args) {

        *//*Socket client = null;
        InetAddress ipAdress = client.getInetAddress();
        EntityManager em = emf.createEntityManager();

        UserInfo user = new UserInfo(ipAdress);

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();*//*


        try {
            Socket socket = new Socket("192.168.1.155", 5050);

            InetAddress ipAdress = socket.getInetAddress();
            EntityManager em = emf.createEntityManager();

            UserInfo user = new UserInfo(ipAdress);

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            em.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

