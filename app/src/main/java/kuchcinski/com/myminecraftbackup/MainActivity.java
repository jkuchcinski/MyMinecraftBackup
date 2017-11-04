package kuchcinski.com.myminecraftbackup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    int notifyID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                CharSequence text = button.getText();

                File root = new File("/sdcard/games/com.mojang/minecraftWorlds");/*KBIAAMx9AQA=*/ //Environment.getRootDirectory();//Environment.getExternalStorageDirectory(); //Environment.getDataDirectory();  //Environment.getRootDirectory(); //new File();

                final TextView textView = findViewById(R.id.textView);

                String log = new String();

                log+="\npath="+root.getAbsolutePath();
                log+="\nexists="+root.exists();
                log+="\ndirectory="+root.isDirectory();
                log+="\nStorage writeable="+IOUtils.isExternalStorageWritable();
                log+="\nStorage readable="+IOUtils.isExternalStorageReadable()+"\n";

                if (root.exists() && root.listFiles()!=null) {
                    for (File file : root.listFiles()) {
                        if (file.isDirectory()) {
                            log += "\n" + getString(new File(file.getAbsolutePath()+"/levelname.txt")) + " ["+file.getName() + "]";
                        }
                    }
                }
                textView.setText(log);

                log+="\nCreating backup zip...";
                textView.setText(log);

                String backupLocation = "/sdcard/Download/MinecraftBackup-"+System.currentTimeMillis()+".zip";

                ZIPUtils.zipFileAtPath("/sdcard/games/com.mojang/", backupLocation);



                if (new File(backupLocation).exists()) {
                    String message ="Backup has been created in "+backupLocation+", size="+(new File(backupLocation).length());
                    notifyAboutBackup(message, false);
                    log+="\n"+message;
                } else {
                    String message ="Backup could not be created in "+backupLocation;
                    notifyAboutBackup(message, true);
                    log+="\n"+message;
                }

                textView.setText(log);
            }
        });
    }

    @NonNull
    private String getString(File levelname) {
        String result = "";
        try {

            InputStream in_s = new FileInputStream(levelname);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            result = new String(b);
        } catch (Exception e) {
            // e.printStackTrace();
            result = "Error: can't show file.";
        }
        return result;
    }


    public void notifyAboutBackup2(String message, boolean error) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, ShowNotificationDetailActivity.class), 0);
        Resources r = getResources();
        String CHANNEL_ID = "my_channel_01";
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setTicker("Notification Title - Ticker")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Notification Title")
                .setContentText("Notification Text")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, notification);
        notifyID++;
    }

    private void notifyAboutBackup(String message, boolean error) {
            // The id of the channel.
            String CHANNEL_ID = "my_channel_01";
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(android.R.drawable.ic_menu_save)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!");
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, ShowNotificationDetailActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your app to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // mNotificationId is a unique integer your app uses to identify the
            // notification. For example, to cancel the notification, you can pass its ID
            // number to NotificationManager.cancel().
            mNotificationManager.notify(notifyID, mBuilder.build());
            notifyID++;
        }


}
