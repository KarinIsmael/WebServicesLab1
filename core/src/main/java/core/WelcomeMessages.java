package core;

import message.TypeOfUser;
import message.Welcome;

import java.util.ServiceLoader;

public class WelcomeMessages {
    static String mes;


    static String welcomeOldUser() {
        ServiceLoader<Welcome> welcomes = ServiceLoader.load(Welcome.class);

        System.out.println("Hello From Welcome");
        for (Welcome welcome : welcomes) {

            TypeOfUser annotation = welcome.getClass().getAnnotation(TypeOfUser.class);
            if (annotation != null && annotation.value().equals("/old")) {

                mes = welcome.welcome();
                //System.out.println(welcome.welcome());
            }
        }
        return mes;
    }

    static String welcomeNewUser() {
        ServiceLoader<Welcome> welcomes = ServiceLoader.load(Welcome.class);
        for (Welcome welcome : welcomes) {

            TypeOfUser annotation = welcome.getClass().getAnnotation(TypeOfUser.class);
            if (annotation != null && annotation.value().equals("/new")) {

                mes = welcome.welcome();
                //System.out.println((mes));
            }
        }
        return mes;
    }
}
