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
    private String name;
    @Id
    private String ipAddress;

    public UserInfo() {
    }

   public UserInfo(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public UserInfo(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public UserInfo(InetAddress ipAdress) {
    }

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return ipAddress.equals(userInfo.ipAddress) && Objects.equals(name, userInfo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ipAddress);
    }
}
