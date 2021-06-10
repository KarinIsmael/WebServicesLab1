package welcomeMessage;

import message.TypeOfUser;
import message.Welcome;

@TypeOfUser("/old")
public class WelcomeMessage implements Welcome {
    @Override
    public String welcome() {
        return "Welcome back to the server!";
    }
}
