package com.example.my_contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {
    EditText email, password;
    Button loginButton;
    private ProgressDialog progressDialog;

    /*   private static final String REQUIRED = "Required";*/

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAuth = FirebaseAuth.getInstance();
        signIn();
        register();

    }
    public void register(){
        TextView registerButton = (TextView)findViewById(R.id.signUp);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(StartActivity.this, Register.class);
                startActivity(regIntent);
            }
        });
    }
    private void signIn(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        progressDialog = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_email = email.getText().toString();
                String s_password = password.getText().toString();

                if (!TextUtils.isEmpty(s_email) || !TextUtils.isEmpty(s_password)){
                    progressDialog.setTitle("Logging in");
                    progressDialog.setMessage("Please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show(); // depricated method
                    loginUser(s_email,s_password);
                }
                /*if (TextUtils.isEmpty(s_email)) {
                    email.setError(REQUIRED);
                }
                if (TextUtils.isEmpty(s_password)) {
                    password.setError(REQUIRED);
                }*/

            }
        });
    }
    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainIntent);
                            finish();

                        }else{
                            progressDialog.hide();
                            Toast.makeText(StartActivity.this, "Invalid Credentials. Please try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
