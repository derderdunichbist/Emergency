package org.hsrt.mc.emergency.user;

/**
 * Created by KA on 14.05.2016.
 */
public class Contact {
    private String name;
    private String email;
    private String phoneNumber;
    private boolean isFavourite;

    public Contact() {

    }


    public Contact (String name, String email, String phoneNubmer, boolean isFavourite){
        this.setName(name);
        this.setEmail(email);
        this.setPhoneNumber(phoneNubmer);
        this.setFavourite(isFavourite);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
