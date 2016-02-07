package ru.alekseymitkin.messenger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.alekseymitkin.messenger.var.pref;
import ru.alekseymitkin.messenger.var.varURL;

public class getNewMessageService extends Service {
    Firebase firebase;
    private int NOTIFY_ID = 1;
    private SharedPreferences mSetting;
    public static List<person> persons;

    public getNewMessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebase.setAndroidContext(getApplicationContext());
        firebase = new Firebase(varURL.URL_FIREBASE);
        persons = new ArrayList<>();
        mSetting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        firebase.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                person person = dataSnapshot.getValue(person.class);
                mSetting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences uid = getSharedPreferences(pref.UID, MODE_PRIVATE);
                boolean notification = mSetting.getBoolean("enabledNotification", true);
                Log.d("321", uid.getString(pref.UID, " ") + " ");
                Log.d("321", person.getMessage() + " " + person.getName());

                if (notification) {
                    try {
                        if (!uid.getString(pref.UID, "").equals(person.getUid())) {
                            MainActivity.adapter.add(person);
                            notification(person);
                        }
                    } catch (NullPointerException e) {
                        Log.d("notification", e.getMessage() + "");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void notification(person person) {
        String text = null;
        text = person.getMessage();
        int icon = R.drawable.message_text, smallIcon = R.drawable.message_text;


        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            builder = new Notification.Builder(context);


            builder.setContentIntent(contentIntent)
                    .setSmallIcon(smallIcon)
                    .setLargeIcon(BitmapFactory.decodeResource(res, icon))
                    .setTicker("Сообщение")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(person.getName());


            Notification notification = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                notification = new Notification.BigTextStyle(builder).bigText(text).build();

                builder.setContentText(person.getMessage());

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                notification = builder.getNotification();
                builder.setContentText(text);
            }
            boolean sound = mSetting.getBoolean("enabledSound", true);
            boolean vibration = mSetting.getBoolean("enabledVibration", true);


            if (vibration||sound) {
                notification.defaults = Notification.DEFAULT_VIBRATE|Notification.DEFAULT_SOUND;
            }
            notification.priority = Notification.PRIORITY_DEFAULT;

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);

        }
    }
}
