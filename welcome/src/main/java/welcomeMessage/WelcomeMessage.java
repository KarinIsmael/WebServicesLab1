package welcomeMessage;

import interf.Welcome;

public class WelcomeMessage implements Welcome {
    @Override
    public String welcome(String name) {
        return "Welcome to the server " + name + "!";
    }
}
