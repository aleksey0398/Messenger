package ru.alekseymitkin.messenger;

/**
 * Created by Митькин on 07.02.2016.
 */
public class person {
    private String name;
    private String message;
    private String uid;

    public person() {
    }

    public person(String name, String message,String uid) {
        this.name = name;
        this.uid = uid;
        this.message = message;
    }
    public person(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setUid(String uid){this.uid = uid;}
    public String getUid(){return uid;}

}
