package org.hsrt.mc.emergency.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KA on 23.06.2016.
 */
public interface User {

    public void setFirstName(String name);

    public void setLastName (String name);

    public void setDateOfBirth(Date dateOfBirth);

    public void setBloodType (String bloodType);

    public void addContact(Contact contact);

    public void addMedication(Medication medication);

    public void addDisease(String disease);

    public void addSpecialNeed(String specialNeed);

    public void removeContact(Contact contact);

    public void removeMedication(Medication medication);

    public void removeDisease(String disease);

    public void removeSpecialNeed(String specialNeed);

    public String getFirstName();

    public String getLastName();

    public Date getDateOfBirth();

    public String getBloodType();

    public List<Contact> getContacts();

    public String getContactsAsString();

    public String getMedication();

    public String getDiseases();

    public String getSpecialNeeds();

}
