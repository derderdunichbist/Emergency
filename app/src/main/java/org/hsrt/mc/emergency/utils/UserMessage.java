package org.hsrt.mc.emergency.utils;

import android.content.Context;
import android.widget.Toast;

import org.hsrt.mc.emergency.gps.GPS;
import org.hsrt.mc.emergency.user.User;

/**
 * Created by David on 28.05.2016.
 */
public class UserMessage {
    private String emergencyMessage;
    private String location;
    private User u;
    private Boolean successful = true;


    public UserMessage(Context con){
        u = new User();
        u.initTestData();

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

        emergencyMessage += "Hallo, mein Name ist " + u.getFirstName() + " " + u.getLastName() + ". Ich benötige Hilfe. Mein aktueller Standort : ";
        emergencyMessage += location + "\n\n";

        if (u.getDiseases().equals("")){
            emergencyMessage +="Ich habe keine Krankheiten hinterlegt\n";
        }
        else{
            emergencyMessage += "Ich habe folgende Krankheiten: " + u.getDiseases() + "\n";
        }
        if (u.getSpecialNeeds().equals("")){
            emergencyMessage += "Meine Besonderheiten: Keine besondere Bedürfnisse hinterlegt";
        }
        else{
            emergencyMessage += "Meine Besonderheiten: " + u.getSpecialNeeds() + "\n";
        }
        if (u.getMedication().equals("")){
            emergencyMessage += "\nIch nehme keine Medikamente ein";
        }
        else{
            emergencyMessage += "\nMedikamente die ich einnehme:\n";
            emergencyMessage += u.getMedication();
        }
        if (u.getBloodType() != null){
            emergencyMessage += "\nMeine Blutgruppe: ";
            emergencyMessage += u.getBloodType();
        }
        else{
            emergencyMessage += "\nIch habe keine Blutgruppe hinterlegt\n";
        }
        System.out.println(emergencyMessage);
    }


    //return the hole EmergencyMessage of the user
    public String getEmergencyMessage(){
        return emergencyMessage;
    }

    //return true if gps is successful
    public Boolean getSuccessful(){
        return successful;
    }



}
