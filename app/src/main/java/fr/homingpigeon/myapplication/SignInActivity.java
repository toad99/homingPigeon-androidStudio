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

import fr.homingpigeon.backend.Client;

public class SignInActivity extends AppCompatActivity {

    Button mLogin;
    Button mSignup;
    EditText mUsernameField;
    EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setTitle("Sign in");
        setContentView(R.layout.activity_sign_in);
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
                        Client c = new Client(null);//TODO FAIRE SETTINGS
                        String result = c.login(username,password);
                        Toast.makeText(SignInActivity.this,result,Toast.LENGTH_LONG).show();
                        if(c.getToken() == null)
                            return;
                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                        intent.putExtra("token",c.getToken());
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                });

        mSignup.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String username = mUsernameField.getText().toString();
                        Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                });

    }

}