package com.example.contactapp_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactapp_v3.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, phone, name, password, confirmPassword;
    private Button signUpButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.READ_PHONE_STATE)){
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.CALL_PHONE, Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }

        mAuth = FirebaseAuth.getInstance();

        backToSignIn();
        createNewAccount();
    }

    private void backToSignIn(){
        TextView signIn;
        signIn= findViewById(R.id.signUpToLogin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(RegisterActivity.this, StartActivity.class);
                startActivity(regIntent);
            }
        });
    }
    private void createNewAccount(){
        email = (EditText)findViewById(R.id.SignUpEmail);
        phone = findViewById(R.id.SignUpNumber);
        name = (EditText)findViewById(R.id.SignUpName);
        password = (EditText)findViewById(R.id.SignUpPassword);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        signUpButton = (Button)findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dispaly_name = name.getText().toString();
                String phone_no = phone.getText().toString();
                String display_email = email.getText().toString();
                String display_password = password.getText().toString();
                String con_password = confirmPassword.getText().toString();

                if(TextUtils.isEmpty((dispaly_name))){
                    name.setError( "Name is required!" );
                }

                if(TextUtils.isEmpty((phone_no))){
                    phone.setError( "Number is required!" );
                }
                if(TextUtils.isEmpty((display_email))){
                    email.setError( "Email is required!" );
                }

                if(TextUtils.isEmpty((display_password))){
                    phone.setError( "Password is required!" );
                }

                if (!display_password.equals(con_password)){
                    Toast.makeText(RegisterActivity.this, "Password does not match",
                            Toast.LENGTH_LONG).show();
                }else {
                    registerUser(dispaly_name, phone_no, display_email, display_password);
                }
            }
        });
    }

    public String readDefaultPhoneNo() {
        TelephonyManager telephonyManager = (TelephonyManager) RegisterActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            return "--";
        }

        String phone = telephonyManager.getLine1Number();
        //Toast.makeText(RegisterActivity.this, phone, Toast.LENGTH_LONG).show();
        return phone;
    }

    private void registerUser(final String name, final String phone, final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    User user = new User(name,email,phone);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user);

                    Toast.makeText(RegisterActivity.this, "Authentication successful.",
                            Toast.LENGTH_LONG).show();
                   // finish();
                    Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                    startActivity(intent);

                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(RegisterActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Permission not granted", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }
}
