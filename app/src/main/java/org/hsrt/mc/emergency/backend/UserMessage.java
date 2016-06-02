package org.hsrt.mc.emergency.backend;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by David on 28.05.2016.
 */
public class UserMessage {
    private String emergencyMessage;
    private String location;
    private User u;
    private Boolean successful = true;



    public UserMessage(Context con){
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
                }
                // msg = new UserMessage(?USER?, location);
            }

        } else {
            gps.showSettings();
        }


        User u = new User();
        emergencyMessage += "Hallo, ich bin ein armer Kosta helft mir, Ich befinde mich: ";

        emergencyMessage += location;
        if (u.getMedication() != null){
            emergencyMessage += "Medikamente die ich einnehme";
            //emergencyMessage += u.getMedication();
        }
        if (u.getBloodType() != null){
            emergencyMessage += "Meine Blutgruppe: ";
            //emergencyMessage += u.getBloodType();
        }

    }



    public String getEmergencyMessage(){
        return emergencyMessage;
    }

    public Boolean getSuccessful(){
        return successful;
    }



}
