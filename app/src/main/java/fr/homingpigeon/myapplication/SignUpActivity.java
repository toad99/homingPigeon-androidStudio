package fr.homingpigeon.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;

import fr.homingpigeon.backend.Client;


public class SignUpActivity extends AppCompatActivity {

    Button mSignup;
    EditText mUsernameField;
    EditText mPasswordField1;
    EditText mPasswordField2;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PVT = "pvt";

    private String public_key;
    private String private_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent i = getIntent();

        mUsernameField = (EditText)findViewById(R.id.usernameField);
        mUsernameField.setText(i.getStringExtra("username"));
        mSignup = (Button)findViewById(R.id.signUpButton);
        mPasswordField1 = (EditText)findViewById(R.id.password1Field);
        mPasswordField2 = (EditText)findViewById(R.id.password2Field);

        mSignup.setOnClickListener(
                new View.OnClickListener()
                {
                    @SuppressLint("NewApi")
                    public void onClick(View view)
                    {
                        String username = mUsernameField.getText().toString();
                        String password1 = mPasswordField1.getText().toString();
                        String password2 = mPasswordField2.getText().toString();

                        if(!password1.equals(password2)) {
                            Toast.makeText(SignUpActivity.this,"Passwords are not the same !",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(password1.length() > 32 || password1.length() < 8) {
                            Toast.makeText(SignUpActivity.this,"Password must have between 8 and 32 characters !",Toast.LENGTH_LONG).show();
                            return;
                        }

                        try {
                            byte[][] keyPairBytes = new byte[2][];
                            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
                            gen.initialize(1024, new SecureRandom());
                            KeyPair pair = gen.generateKeyPair();
                            keyPairBytes[0] = pair.getPrivate().getEncoded();
                            keyPairBytes[1] = pair.getPublic().getEncoded();
                            private_key = Base64.getEncoder().encodeToString(keyPairBytes[0]);
                            public_key = Base64.getEncoder().encodeToString(keyPairBytes[1]);

                            /*System.out.println(private_key.length());
                            System.out.println(public_key.length());
                            System.out.println(private_key);
                            System.out.println(public_key);*/

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchProviderException e) {
                            e.printStackTrace();
                        }

                        Client c = new Client(null);
                        String result = c.signUp(username,password1,public_key);
                        Toast.makeText(SignUpActivity.this,result,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                });
    }

    public void savePrivateKey() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PVT, private_key);
        editor.apply();
        Toast.makeText(this, "Private key saved", Toast.LENGTH_SHORT).show();
    }

    public String loadPrivateKey() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(PVT, "ERROR ! THE PRIVATE KEY WAS NOT FOUND");
    }
}