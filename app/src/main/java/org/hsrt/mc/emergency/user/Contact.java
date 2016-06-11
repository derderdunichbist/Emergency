package org.hsrt.mc.emergency.user;

/**
 * Created by KA on 14.05.2016.
 */
public class Contact {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private ePriority priority;

    public Contact() {

    }


    public Contact (String firstName, String lastName, String email, String phoneNubmer, ePriority priority){
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPhoneNumber(phoneNubmer);
        this.setPriority(priority);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public ePriority getPriority() {
        return priority;
    }

    public void setPriority(ePriority priority) {
        this.priority = priority;
    }

    public String getCompleteName() {
        return this.firstName + " " + this.lastName;
    }

    public void setNameFromCompleteName(String name) {

            String[] names = name.split(" ");
            this.firstName = names[0];

            if(names.length > 1) {
                this.lastName = names[1];
            }
    }
}
