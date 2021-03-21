package fr.homingpigeon.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mLogin;
    Button mSignup;
    EditText mUsernameField;
    EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        mLogin = (Button)findViewById(R.id.login);
        mSignup = (Button)findViewById(R.id.signup);
        mUsernameField = (EditText)findViewById(R.id.usernameField);
        mPasswordField = (EditText)findViewById(R.id.password1Field);

        mLogin.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String username = mUsernameField.getText().toString();
                        String password = mPasswordField.getText().toString();
                        Log.v("username", username);
                        Log.v("password", password);
                        Client c = new Client(false);
                        String result = c.login(username,password);
                        Toast.makeText(MainActivity.this,result,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this,MessengerActivity.class);
                        startActivity(intent);
                    }
                });

        mSignup.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                        startActivity(intent);
                    }
                });

    }

}