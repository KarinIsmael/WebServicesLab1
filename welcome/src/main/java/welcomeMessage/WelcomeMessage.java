package welcomeMessage;

import interf.TypeOfUser;
import interf.Welcome;

@TypeOfUser("/old")
public class WelcomeMessage implements Welcome {
    @Override
    public String welcome() {
        return "Welcome to the server!";
    }
}
