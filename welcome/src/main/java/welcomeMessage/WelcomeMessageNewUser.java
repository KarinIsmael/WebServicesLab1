package welcomeMessage;

import interf.TypeOfUser;
import interf.Welcome;

public class WelcomeMessageNewUser {

    @TypeOfUser("/new")
    public class WelcomeMessage implements Welcome {
        @Override
        public String welcome() {
            return "Welcome back to the server!";
        }
    }
}
