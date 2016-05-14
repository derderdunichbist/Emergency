package org.hsrt.mc.emergency.backend;

import java.util.Date;
import java.util.List;

/**
 * Created by KA on 14.05.2016.
 */
public class User {

    private String firstName;
    private String lastName;
    private String bloodType;
    private Date dateOfBirth;
    private List<Medication> medication;
    private List<String> diseases;
    private List<String> specialNeeds;
    private List<Contact> contacts;
}
