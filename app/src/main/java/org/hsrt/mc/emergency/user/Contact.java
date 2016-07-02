package org.hsrt.mc.emergency.user;

import android.widget.EditText;

/**
 * Created by KA on 14.05.2016.
 */
public class Contact {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private boolean isFavourite;


    public Contact() {

    }

    public Contact (String name, String email, String phoneNumber, boolean isFavourite){
        this.setName(name);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if(o instanceof Contact) {
            Contact contact = (Contact)o;
            if(contact.getName().equals(this.getName()) && contact.getEmail().equals(this.getEmail()) && contact.getPhoneNumber().equals(this.getPhoneNumber())) {
                isEqual = true;
            }
        }
        return isEqual;
    }

    @Override
    public Contact clone() {
        Contact contact = new Contact(this.name, this.email, this.phoneNumber, this.isFavourite);
        contact.setId(this.id);
        return contact;
    }
}
