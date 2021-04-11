package fr.homingpigeon.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
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
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
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

                    @RequiresApi(api = Build.VERSION_CODES.O)
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
                            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
                            gen.initialize(1024, new SecureRandom());
                            KeyPair pair = gen.generateKeyPair();
                            keyPairBytes[0] = pair.getPrivate().getEncoded();
                            keyPairBytes[1] = pair.getPublic().getEncoded();
                            private_key = Base64.getEncoder().encodeToString(keyPairBytes[0]);
                            public_key = Base64.getEncoder().encodeToString(keyPairBytes[1]);
                            savePrivateKey(username);
                            Log.d("pvt",loadPrivateKey(username));

                        } catch (NoSuchAlgorithmException e) {
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

    public void savePrivateKey(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username, private_key);
        editor.apply();
        Toast.makeText(SignUpActivity.this, "Private key saved", Toast.LENGTH_SHORT).show();
    }

    public String loadPrivateKey(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String s = sharedPreferences.getString(username, "");
        Log.d("pvt",s);
        return s;
    }
}