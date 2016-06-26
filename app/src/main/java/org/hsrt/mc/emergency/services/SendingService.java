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
import org.hsrt.mc.emergency.utils.UserMessage;

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

        // build notification (icon,title,text)
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Emergency-Call")
                        .setContentText(userMessage.getEmergencyMessage());

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //show notification
        mNotificationManager.notify(123, mBuilder.build()); // Show an build notification with id=123

         // send SMS-Message
        String text = userMessage.getEmergencyMessage();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("015787405462", null, "EMERGENCY-SMS: " + text, null, null); // send Message, put your test number here

        // // Make Phone-Call Intent
       // Intent callIntent = new Intent(Intent.ACTION_CALL);
       // callIntent.setData(Uri.parse("tel:015787405462"));
       // callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


       try{
       //    startActivity(callIntent);
       }
       catch(Exception e){
           System.out.print(e.getMessage());
       }

    }
}
