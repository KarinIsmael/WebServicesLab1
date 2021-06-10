package core;

import com.google.gson.Gson;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static List<String> synchronised = new ArrayList<>();

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("user");

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(5050)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("=======================================");
                executorService.submit(() -> handleConnection(client));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        emf.close();
    }

    private static void handleConnection(Socket client) {

        try {
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader((client.getInputStream())));

            String url = requestHandler(inputFromClient);
            OutputStream outputToClient = client.getOutputStream();

            if (url.equals("/hej")) {
                sendTxtResponse(outputToClient);

            } else if (url.equals("/apa")) {
                sendImageResponse(outputToClient);

            } else if (url.equals("/morningface")) {
                sendOrangutangResponse(outputToClient);

            } else if (url.equals("/user-ips")) {
                DatabaseManagement.sendIpAdresses(outputToClient);

            } else if (url.contains("/sendmessage/"))
                DatabaseManagement.saveMessage(url, outputToClient);

            else {
                output(client);
            }

           DatabaseManagement.saveIpToDatabase(client, outputToClient);

            outputToClient.flush();
            inputFromClient.close();
            outputToClient.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(client.getInetAddress());
        System.out.println(Thread.currentThread().getName());
    }

    private static void sendOrangutangResponse(OutputStream outputToClient) throws IOException {

        File find = Path.of("core", "target", "classes", "Orangutang.jpg").toFile();

        fileImporter(outputToClient, find);

    }

    private static void sendImageResponse(OutputStream outputToClient) throws IOException {

        File find = Path.of("core", "target", "classes", "apa.png").toFile();

        fileImporter(outputToClient, find);

    }

    private static void fileImporter(OutputStream outputToClient, File find) throws IOException {

        FileInputStream fileInput = new FileInputStream(find);

        byte[] data = new byte[(int) find.length()];
        fileInput.read(data);

        String contentType = Files.probeContentType(find.toPath());
        String header = "HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\nContent-length: " + data.length + "\r\n\r\n";

        outputToClient.write(header.getBytes());
        outputToClient.write(data);
        outputToClient.flush();

    }

    private static void sendTxtResponse(OutputStream outputToClient) throws IOException {
        File find = Path.of("core", "target", "classes", "Hej.txt").toFile();

        fileImporter(outputToClient, find);
    }

    public static void getGson(OutputStream outputToClient, Object message) throws IOException {

        Gson gson = new Gson();
        String json = gson.toJson(message);
        System.out.println(json);
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        String header = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-length: " + data.length + "\r\n\r\n";

        outputToClient.write(header.getBytes());
        outputToClient.write(data);
        outputToClient.flush();
    }

    private static void output(Socket client) throws IOException {

        PrintWriter outputToClient = new PrintWriter(client.getOutputStream());
        outputToClient.print("HTTP/1.1 404 Not Found\r\nContent-length: 0\r\n\r\n");

        outputToClient.flush();

    }

    private static String requestHandler(BufferedReader inputFromClient) throws IOException {
        String url = "";

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
        synchronized (synchronised) {
            synchronised.addAll(templist);
        }
        return url;
    }

}

