module client {
    requires java.persistence;

    opens user to com.google.gson, org.hibernate.orm.core;
    exports user;
}