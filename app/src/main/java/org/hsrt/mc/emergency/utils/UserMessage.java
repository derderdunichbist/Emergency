package org.hsrt.mc.emergency.utils;

import android.content.Context;
import android.widget.Toast;

import org.hsrt.mc.emergency.gps.GPS;
import org.hsrt.mc.emergency.user.Medication;
import org.hsrt.mc.emergency.user.User;
import org.hsrt.mc.emergency.user.UserImplementation;

import java.util.List;

/**
 * Created by David on 28.05.2016.
 */
public class UserMessage {
    private String emergencyMessage;
    private String location;
    private User u;
    private Boolean successful = true;
    private String allDiseases;
    private String allSpecialNeeds;
    private String allMedicationText;


    /**
     *Implement the UserMessage Text
     * @param con
     */
    public UserMessage(Context con){
        u = UserImplementation.getUserObject();

        List<String> diseases = u.getDiseases();
        this.allDiseases = getDiseasesAsString(diseases);

        List<String> specialNeeds = u.getSpecialNeeds();
        this.allSpecialNeeds = getSpecialNeedsAsString(specialNeeds);

        List<Medication> medication = u.getMedication();
        this.allMedicationText = getMedicationAsString(medication);

        GPS gps = new GPS(con);
        if(gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            if(latitude == 0.0 && longitude == 0.0){
                //Toast.makeText(con, "Bitte erlauben Sie die Standort-Berechtigung", Toast.LENGTH_LONG).show();
                successful = false;
                //message input
            }else{
                try{
                    location = gps.getGeoLocation(latitude,longitude);

                } catch (Exception e) {
                    Toast.makeText(con, "Unable to detect location", Toast.LENGTH_SHORT).show();
                    successful = false;
                }
                // msg = new UserMessage(?USER?, location);
            }
        } else {
            gps.showSettings();
        }

        emergencyMessage = "";
        emergencyMessage += "Name: " + u.getFirstName() + " " + u.getLastName() + ". Standort: ";
        emergencyMessage += location;

        if (this.allDiseases.equals("")){
            emergencyMessage +="";
        }
        else{
            emergencyMessage += "/ Krankheiten: " + this.allDiseases + "\n";
        }
        if (this.allSpecialNeeds.equals("")){
            emergencyMessage += "";
        }
        else{
            emergencyMessage += "/ Besonderheiten: " + this.allSpecialNeeds + "\n";
        }
        if (this.allMedicationText.equals("")){
            emergencyMessage += "";
        }
        else{
            emergencyMessage += "/ Medikamente:\n";
            emergencyMessage += this.allMedicationText;
        }
        if (u.getBloodType() != null){
            emergencyMessage += "/ Meine Blutgruppe: ";
            emergencyMessage += u.getBloodType();
        }
        else{
            emergencyMessage += "";
        }
        System.out.println(emergencyMessage);
    }

    private String getMedicationAsString(List<Medication> medication) {
        String allMedicationText = "";
        for( Medication m: medication )
        {
            allMedicationText += m.getMedicationText();
            allMedicationText += "\n";
        }
        return allMedicationText;
    }

    private String getSpecialNeedsAsString(List<String> specialNeeds) {
        String allSpecialNeeds = "";
        for (String s: specialNeeds){
            allSpecialNeeds += s;
            allSpecialNeeds += ", ";
        }
        return allSpecialNeeds;
    }

    private String getDiseasesAsString(List<String> diseases) {
        String allDiseases = "";
        for ( String d: diseases){
            allDiseases += d;
            allDiseases += ", ";
        }
        return allDiseases;
    }


    /**
     *
     * @return the hole EmergencyMessage of the use
     */
    public String getEmergencyMessage(){
        return emergencyMessage;
    }

    /**
     *
     * @return true if gps is successful
     */
    public Boolean getSuccessful(){
        return successful;
    }

    public User getUser(){
        return this.u;
    }



}
