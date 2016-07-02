package org.hsrt.mc.emergency.user;

import android.widget.Toast;

import org.hsrt.mc.emergency.activities.MainActivity;
import org.hsrt.mc.emergency.persistence.MySQLiteHelper;
import org.hsrt.mc.emergency.persistence.UserDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KA on 14.05.2016.
 */
public class UserImplementation implements User{

    private String firstName;
    private String lastName;
    private String bloodType;
    private String dateOfBirth;
    private String gender;
    private List<Medication> medication = new ArrayList<>();
    private List<String> diseases = new ArrayList<>();
    private List<String> specialNeeds = new ArrayList<>();
    private UserDAO userDAO;
    private List<Contact> contacts = new ArrayList<>();


    /**
     * Singleton constant
     */
    private static User userObject;


    public UserImplementation(){

    }

    public UserImplementation(UserDAO userDAO) {
        this.userDAO = userDAO;
        userObject = this;
    }

    @Override
    public List<String> getDiseases(){
        return this.diseases;
    }

    @Override
    public List<String> getSpecialNeeds(){
    return specialNeeds;
}

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        userDAO.updateUserFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
        userDAO.updateUserLastName(lastName);
    }

    @Override
    public String getBloodType() {
        return bloodType;
    }

    @Override
    public List<Contact> getContacts() {
        return contacts;
    }

    @Override
    public List<Medication> getMedication() {
        return this.medication;
    }

    @Override
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
        userDAO.updateUserBloodType(bloodType);
    }

    @Override
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        userDAO.updateUserDob(dateOfBirth);
    }

    @Override
    public void addContact(Contact contact) {
        if(contacts.size() < 3) {
            if(contacts.size() == 0) {
                if(userDAO.insertContactIntoDatabase(contact)) {
                    this.contacts.add(contact);
                }
            } else {
                if(userDAO.insertContactIntoDatabase(contact)) {
                    this.contacts.add(contact);
                }
            }
        } else {
            Toast.makeText(userDAO.getContext(), "Internal Error, no more thant 3 contacts allowed", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void addMedication(Medication med) {
        if(medication.size() == 0) {
            if(userDAO.insertMedicationIntoDatabase(med)) {
                this.medication.add(med);
            }
        } else {
            if(userDAO.insertMedicationIntoDatabase(med)) {
                this.medication.add(med);
            }
        }
    }

    @Override
    public void addSpecialNeed(String specialNeed) {
       if(specialNeeds.size() == 0) {
            if(userDAO.insertSpecialNeedIntoDatabase(specialNeed)) {
                this.specialNeeds.add(specialNeed);
            }
        } else {
            if(userDAO.insertSpecialNeedIntoDatabase(specialNeed)) {
                this.specialNeeds.add(specialNeed);
            }
        }
    }

    @Override
    public void addDisease(String disease) {
        if(diseases.size() == 0) {
            if(userDAO.insertDiseaseIntoDatabase(disease)) {
                this.diseases.add(disease);
            }
        } else {
            if(userDAO.insertDiseaseIntoDatabase(disease)) {
                this.diseases.add(disease);
            }
        }
    }

    @Override
    public void removeContact(Contact contact) {
        userDAO.deleteContactFromDatabase(contact);
        for(Contact c : this.contacts) {
            if(contact.equals(c)) {
                this.contacts.remove(c);
                return;
            }
        }
    }

    @Override
    public void removeMedication(Medication medication) {
        userDAO.deleteMedicationFromDatabase(medication);
        for(Medication m : this.medication) {
            if(medication.equals(m)) {
                this.medication.remove(m);
                return;
            }
        }
    }

    @Override
    public void removeSpecialNeed(String specialNeed) {
        userDAO.deleteSpecialNeedFromDatabase(specialNeed);
        for(String s : this.specialNeeds) {
            if(specialNeed.equals(s)) {
                this.specialNeeds.remove(s);
                return;
            }
        }
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
        userDAO.updateGender(gender);
    }

    @Override
    public String getGender() {
        return this.gender;
    }

    @Override
    public void removeDisease(String disease) {
        userDAO.deleteDiseaseFromDatabase(disease);
        for (String s : this.diseases) {
            if (disease.equals(s)) {
                this.diseases.remove(s);
                return;
            }
        }
    }

    /**
     * Initializes the user Object with the values from the database. Called only once per run at
     * the start of the application
     * @return the User with correctly assigned values from the database
     */
    public static User initUserObjectFromDatabase() {
        UserDAO.getUserDAO().buildUserObjectFromDatabase(userObject);

        return userObject;
    }

    public static User getUserObject() {
        return userObject;
    }

}
