package org.hsrt.mc.emergency.backend;

import android.util.Log;

/**
 * Created by David on 28.05.2016.
 */
public class UserMessage {
    private String emergencyMessage;
    private String position;
    private Medication m;
    private User u;


    public UserMessage(User u){
        this.u = u;
        //this.position =
        emergencyMessage += "Hallo, ich bin ein armer Kosta helft mir, Ich befinde mich: ";
        emergencyMessage += position;
        if (u.getMedication() != null){
            emergencyMessage += "Medikamente die ich einnehme";
            emergencyMessage += u.getMedication();
        }
        if (u.getBloodType() != null){
            emergencyMessage += "Meine Blutgruppe: ";
            emergencyMessage += u.getBloodType();
        }

        Log.d("STATE", "in UserMessage");
        System.out.println("InUserMessage");
    }



    public String sendEmergencyMessage(){
        return emergencyMessage;
    }



}
