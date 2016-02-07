package ru.alekseymitkin.messenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import ru.alekseymitkin.messenger.var.pref;
import ru.alekseymitkin.messenger.var.varURL;

/**
 * Created by Митькин on 07.02.2016.
 */
public class login extends AppCompatActivity {
EditText name,surname;
    Button buttonLogin;
    Firebase firebase;
    SharedPreferences mSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        name = (EditText)findViewById(R.id.loginName);
        surname = (EditText)findViewById(R.id.loginSurname);
        buttonLogin = (Button)findViewById(R.id.loginButton);
        firebase.setAndroidContext(getApplicationContext());
        firebase = new Firebase(varURL.URL_FIREBASE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()==0){
                    Snackbar.make(v,"Введите Ваше имя",Snackbar.LENGTH_SHORT).show();
                } else if (surname.getText().length() ==0){
                    Snackbar.make(v,"Введите Вашу фамилию",Snackbar.LENGTH_SHORT).show();
                } else {
                    firebase.authAnonymously(new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            mSetting = getSharedPreferences(pref.UID,MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSetting.edit();
                            editor.putString(pref.UID, authData.getUid());
                            editor.apply();
                            mSetting = getSharedPreferences(pref.NAME,MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = mSetting.edit();
                            String nameS = name.getText().toString()+" "+surname.getText().toString();
                            editor1.putString("name", nameS);
                            editor1.apply();

                            finish();
                            startActivity(new Intent(login.this,MainActivity.class));
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
    }
}
