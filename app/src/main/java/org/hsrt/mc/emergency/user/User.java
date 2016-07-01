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

    public void setDateOfBirth(String dateOfBirth);

    public void setBloodType (String bloodType);

    public void addContact(Contact contact);

    public void addMedication(Medication medication);

    public void addDisease(String disease);

    public void addSpecialNeed(String specialNeed);

    public void removeContact(Contact contact);

    public void removeMedication(Medication medication);

    public void removeDisease(String disease);

    public void removeSpecialNeed(String specialNeed);

    public void setGender(String gender);

    public String getGender();

    public String getFirstName();

    public String getLastName();

    public String getDateOfBirth();

    public String getBloodType();

    public List<Contact> getContacts();

    public List<Medication> getMedication();

    public List<String> getDiseases();

    public List<String> getSpecialNeeds();

}
