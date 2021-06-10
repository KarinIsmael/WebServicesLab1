package core;

import interf.TypeOfUser;
import interf.Welcome;
import welcomeMessage.WelcomeMessage;
import welcomeMessage.WelcomeMessageNewUser;

import java.util.ServiceLoader;

public class WelcomeMessages {
    static String mes;

    static ServiceLoader<Welcome> welcomes = ServiceLoader.load(Welcome.class);

    static String welcomeOldUser() {

        for (Welcome welcome : welcomes) {

            TypeOfUser annotation = welcome.getClass().getAnnotation(TypeOfUser.class);
            if(annotation!= null && annotation.value().equals( "/old")) {

                mes = welcome.welcome();
                //System.out.println(welcome.welcome());
            }
        }
        return mes;
    }

    static String welcomeNewUser() {
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
