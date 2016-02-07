package ru.alekseymitkin.messenger;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import ru.alekseymitkin.messenger.var.pref;
import ru.alekseymitkin.messenger.var.varURL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText message;
    SharedPreferences NameSetting, uidSetting;
    Firebase firebase;
    ImageButton imageButton;
    RecyclerView rv;
    private String LOG_ARGS = "main Activity";
    public static RV_holder adapter;
    public static List<person> person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uidSetting = getSharedPreferences(pref.UID, MODE_PRIVATE);
        NameSetting = getSharedPreferences(pref.NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = uidSetting.edit();

        if (!uidSetting.contains(pref.UID)) {
            finish();
            startActivity(new Intent(this, login.class));
        } else
            ServiceIsStart();

        setContentView(R.layout.activity_main);
        message = (EditText) findViewById(R.id.sendMessage_editText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        firebase.setAndroidContext(getApplicationContext());
        firebase = new Firebase(varURL.URL_FIREBASE);
        imageButton = (ImageButton) findViewById(R.id.buttonSend);
        imageButton.setOnClickListener(this);
        toolbar.setLogo(R.drawable.send);

        person = new ArrayList<>();

       // person.add(new person("Привет, как утебя дела, я вот тестирую новый мессенджер","Митькин Алексей","вы"));
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);
        adapter = new RV_holder(person,getApplicationContext());
        rv.setAdapter(adapter);
        setSupportActionBar(toolbar);

    }

    public void ServiceIsStart() {
        boolean tStartService = true;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(100);
        for (int i = 0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            if (getNewMessageService.class.getName().equalsIgnoreCase(rsi.service.getClassName())) {
                tStartService = false;
                break;
            }
        }
        if (tStartService) {
            Log.d(LOG_ARGS, "сервис не запушен - запускаю");
            startService(new Intent(this, getNewMessageService.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, prefNotif.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSend:
                if (message.getText().length() != 0) {
                    firebase.child("message").setValue(new person(NameSetting.getString("name", ""), message.getText().toString(), uidSetting.getString(pref.UID, "")));
                    adapter.add(new person(NameSetting.getString("name", ""), message.getText().toString()));
                     message.setText("");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
