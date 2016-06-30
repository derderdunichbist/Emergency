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
    private Date dateOfBirth;
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


    public void initTestData() {
        this.firstName = "Andy";
        this.lastName = "Müller";
        this.bloodType = BloodType.A_NEG;
        this.dateOfBirth = new Date(1980,5,10);

        this.medication = new ArrayList<Medication>();


        Medication medication1 = new Medication();
        medication1.setName("Vagisil");
        medication1.setManufacturer("Vagisil Inc.");
        medication1.setAmountPerDay(3);
        medication1.setDosis("20g/Use");
        this.medication.add(medication1);

        Medication medication2 = new Medication();
        medication2.setName("Asperin");
        medication2.setManufacturer("DIEFIRMA Inc.");
        medication2.setAmountPerDay(5);
        medication2.setDosis("20g/Use");
        this.medication.add(medication2);

        this.diseases = new ArrayList<String>();
        this.diseases.add("Mamaschnuckler");
        this.diseases.add("Krankheit2");

        this.specialNeeds = new ArrayList<String>();
        this.specialNeeds.add("Schokoladenesser");
        this.specialNeeds.add("Viel Schokoladenesser");
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
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
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
                this.contacts.remove(m);
                return;
            }
        }
    }

    @Override
    public void removeSpecialNeed(String specialNeed) {
        userDAO.deleteSpecialNeedFromDatabase(specialNeed);
        for(String s : this.specialNeeds) {
            if(specialNeed.equals(s)) {
                this.contacts.remove(s);
                return;
            }
        }
    }

    @Override
    public void removeDisease(String disease) {
        userDAO.deleteDiseaseFromDatabase(disease);
        for (String s : this.diseases) {
            if (disease.equals(s)) {
                this.contacts.remove(s);
                return;
            }
        }
    }

    public static User initUserObjectFromDatabase() {
        UserDAO.getUserDAO().buildUserObjectFromDatabase(userObject);

        return userObject;
    }

    public static User getUserObject() {
        return userObject;
    }

}
