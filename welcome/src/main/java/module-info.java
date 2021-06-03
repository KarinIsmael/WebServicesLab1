import interf.Welcome;
import welcomeMessage.WelcomeMessage;

module welcome {
    requires interf;
    provides Welcome with WelcomeMessage;
}