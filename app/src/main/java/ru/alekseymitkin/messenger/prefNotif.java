package ru.alekseymitkin.messenger;

import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by Митькин on 07.02.2016.
 */
public class prefNotif extends PreferenceActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferenses_notification);
    }
}
