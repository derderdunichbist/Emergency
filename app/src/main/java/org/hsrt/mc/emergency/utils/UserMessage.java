package org.hsrt.mc.emergency.utils;

import android.content.Context;
import android.widget.Toast;

import org.hsrt.mc.emergency.R;
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
    private Context con;


    /**
     *Implement the UserMessage Text
     * @param con
     */
    public UserMessage(Context con){
        this.con = con;
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
                successful = false;
                //message input
            }else{
                try{
                    location = gps.getGeoLocation(latitude,longitude);

                } catch (Exception e) {
                    Toast.makeText(con, R.string.unable_detect_location, Toast.LENGTH_SHORT).show();
                    successful = false;
                }
                // msg = new UserMessage(?USER?, location);
            }
        } else {
            gps.showSettings();
        }

        emergencyMessage = "";
        emergencyMessage += con.getString(R.string.emergency_capital);
        emergencyMessage += " - " + con.getString(R.string.need_help_capital)+"\n";
        emergencyMessage += "Name: " + u.getFirstName() + " " + u.getLastName() +", " + con.getString(R.string.position);
        emergencyMessage += location + "\n";

        if (!Verifier.isStringEmptyOrNull(u.getBloodType())){
            emergencyMessage += con.getString(R.string.bloodtype) + " ";
            emergencyMessage += u.getBloodType() + "\n";
        }

        if (!Verifier.isStringEmptyOrNull(this.allDiseases)){
            emergencyMessage += con.getString(R.string.diseases) + ": " + this.allDiseases + "\n";
        }

        if (!Verifier.isStringEmptyOrNull(this.allSpecialNeeds)){
            emergencyMessage += con.getString(R.string.special_needs) + ": " + this.allSpecialNeeds + "\n";
        }

        if (!Verifier.isStringEmptyOrNull(this.allMedicationText)){
            emergencyMessage += con.getString(R.string.medication) + " ";
            emergencyMessage += this.allMedicationText  + "\n";
        }
    }

    private String getMedicationAsString(List<Medication> medication) {
        String allMedicationText = "";
        if(medication.size() == 1) {
            allMedicationText += medication.get(0).getName() + ", dos.: " + medication.get(0).getDosis() + " , " + medication.get(0).getAmountPerDay() + con.getString(R.string.times_per_day);
        } else {
            for( Medication m: medication )
            {
                allMedicationText += m.getName() + ", dos.: " + m.getDosis() + " , " + m.getAmountPerDay() + con.getString(R.string.times_per_day) + "; ";
            }
        }
        return allMedicationText;
    }

    private String getSpecialNeedsAsString(List<String> specialNeeds) {
        String allSpecialNeeds = "";
        if(specialNeeds.size() == 1) {
            allSpecialNeeds += specialNeeds.get(0);
        } else {
            for (String s: specialNeeds){
                allSpecialNeeds += s;
                allSpecialNeeds += ", ";
            }
        }
        return allSpecialNeeds;
    }

    private String getDiseasesAsString(List<String> diseases) {
        String allDiseases = "";
        if(diseases.size() == 1) {
            allDiseases += diseases.get(0);
        } else {
            for (String d: diseases){
                allDiseases += d;
                allDiseases += ", ";
            }
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
