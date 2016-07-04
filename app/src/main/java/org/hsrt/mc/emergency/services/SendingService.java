package org.hsrt.mc.emergency.services;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.user.Contact;
import org.hsrt.mc.emergency.user.UserImplementation;
import org.hsrt.mc.emergency.utils.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 31.05.2016.
 */
public class SendingService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SendingService() {
        super(SendingService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) { //Sending Service Handle with invoke of an intent

        UserMessage userMessage = new UserMessage(this.getApplicationContext());
        List<Contact> contacts = UserImplementation.getUserObject().getContacts();
        String favoriteNumber = "";
        String exceptionMsg = "";


        try {
            // part the SMS in multiple SMS-Messages
            String text = userMessage.getEmergencyMessage();
            List<String> textMessages = splitString(text);

            // send SMS-Messages
            for (int i = 0; i < contacts.size(); i++) {

                for (int j = 0; j < textMessages.size(); j++) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(contacts.get(i).getPhoneNumber(), null, textMessages.get(j), null, null);
                }
                if (contacts.get(i).isFavourite()) {
                    favoriteNumber = contacts.get(i).getPhoneNumber();
                }
            }
        }
        catch (Exception e){
            exceptionMsg +="SMS-Sending Fail,";
        }


            // // Make Phone-Call Intent
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + favoriteNumber));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


       try{
           startActivity(callIntent);
       }
       catch(Exception e){
           //System.out.print(e.getMessage());
           exceptionMsg +="Phone-Call Failed";
       }

        if(exceptionMsg == "") exceptionMsg = "Emergency-Functions successful";

        // build notification (icon,title,text)
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.emergency_call))
                        .setContentText(exceptionMsg);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //show notification
        mNotificationManager.notify(123, mBuilder.build()); // Show an build notification with id=123

    }

    public List<String> splitString(String s) {
        int arrayLength = (int) Math.ceil(((s.length() / (double)160)));
        List<String> result = new ArrayList<>();

        int j = 0;
        int lastIndex = arrayLength - 1;
        for (int i = 0; i < lastIndex; i++) {
            result.add(i,s.substring(j, j + 160));
            j += 160;

        }
        result.add(lastIndex,s.substring(j));

        return result;
    }
}
