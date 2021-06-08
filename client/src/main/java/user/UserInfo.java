package user;

import javax.persistence.*;
import java.net.InetAddress;
import java.util.Objects;

//sätt att skicka namn i server till javaprogrammet.
//header
//url med frågetecken "url parametrar"
//skickar som en post i bodyn

@Entity

public class UserInfo {
    @Id
    private String ipAddress;

    public UserInfo() {
    }

    public UserInfo(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    public String getIpAddress() {
        return ipAddress;
    }

}
