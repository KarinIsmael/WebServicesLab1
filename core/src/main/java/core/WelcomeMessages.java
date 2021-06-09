package core;

import interf.TypeOfUser;
import interf.Welcome;
import welcomeMessage.WelcomeMessage;

import java.util.ServiceLoader;

public class WelcomeMessages {
    static String mes;

    static ServiceLoader<WelcomeMessage> welcomes = ServiceLoader.load(WelcomeMessage.class);

    static String welcomeOldUser() {

        for (WelcomeMessage welcome : welcomes) {

            TypeOfUser annotation = welcome.getClass().getAnnotation(TypeOfUser.class);
            if(annotation!= null && annotation.value().equals( "/old")) {

                mes = welcome.welcome();
                System.out.println(welcome.welcome());
            }
        }
        return mes;
    }

    static void welcomeNewUser() {
        for (WelcomeMessage welcome : welcomes) {

            TypeOfUser annotation = welcome.getClass().getAnnotation(TypeOfUser.class);
            if (annotation != null && annotation.value().equals("/new")) {
                System.out.println(welcome.welcome());
            }
        }
    }
}
