package fr.homingpigeon.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {

    Button mSignup;
    EditText mUsernameField;
    EditText mPasswordField1;
    EditText mPasswordField2;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PUB = "pub";
    public static final String PVT = "pvt";

    Key public_key;
    Key private_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUsernameField = (EditText)findViewById(R.id.usernameField);
        mSignup = (Button)findViewById(R.id.signup);
        mPasswordField1 = (EditText)findViewById(R.id.usernameField);
        mPasswordField2 = (EditText)findViewById(R.id.password1Field);

        mSignup.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String username = mUsernameField.getText().toString();
                        String password1 = mPasswordField1.getText().toString();
                        String password2 = mPasswordField2.getText().toString();

                        if(!password1.equals(password2)) {
                            Toast.makeText(SignUpActivity.this,"Passwords are not the same !",Toast.LENGTH_LONG).show();
                            return;
                        }

                        try {
                            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                            kpg.initialize(1024);
                            KeyPair kp = kpg.generateKeyPair();
                            public_key = kp.getPublic();
                            private_key = kp.getPrivate();

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            return;
                        }

                        Client c = new Client(false);
                        //String result = c.signUp(username,password1,public_key);
                        //Toast.makeText(SignUpActivity.this,result,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    /*
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PUB, public_key.toString());
        editor.putString(PVT, private_key.toString());
        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(PUB, "");
        switchOnOff = sharedPreferences.getBoolean(PVT, false);
    }

    public void updateViews() {
        textView.setText(text);
        switch1.setChecked(switchOnOff);
    }*/
}