package user;

import java.io.IOException;
import java.net.Socket;

public class User {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("192.168.1.155", 5050);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}