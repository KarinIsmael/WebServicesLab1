package welcomeMessage;

import message.TypeOfUser;
import message.Welcome;

public class WelcomeMessageNewUser {

    @TypeOfUser("/new")
    public class WelcomeMessage implements Welcome {
        @Override
        public String welcome() {
            return "Welcome to the server!";
        }
    }
}
