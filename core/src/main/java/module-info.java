module core {
    uses message.Welcome;
    requires java.persistence;
    requires com.google.gson;
    requires client;
    requires net.bytebuddy;
    requires java.sql;
    requires com.fasterxml.classmate;
    requires java.xml.bind;
    requires org.hibernate.orm.core;
    requires message;
    requires Welcome;
}