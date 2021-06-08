package core;

import com.google.gson.Gson;
import user.UserInfo;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DatabaseManagement {
    static void saveMessage(BufferedReader inputFromClient, OutputStream outputToClient) throws IOException {

            EntityManager em = Main.emf.createEntityManager();

            var line = inputFromClient.readLine();

            while(true) {
//                if (line.startsWith("GET") && line.contains("/sendmessage")) {
                if (line.contains("/sendmessage")) {
                    String message = line.split("/sendmessage")[12];

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
}
