package user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost",5050);
            PrintWriter output = new PrintWriter(socket.getOutputStream());

            output.print("GET / HTTP/1.1\r\n");
            output.print("Host: localhost\r\n");
            output.println("Hello From Client Arif\r\n\r\n");
            output.print("\r\n");
            output.flush();

            var inputFromServer = new BufferedReader(new InputStreamReader((socket.getInputStream())));

            while(true){
                var line = inputFromServer.readLine();
                if ( line == null || line.isEmpty()) {
                    break;
                }
                System.out.println(line);
            }
            output.flush();
            inputFromServer.close();
            output.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}