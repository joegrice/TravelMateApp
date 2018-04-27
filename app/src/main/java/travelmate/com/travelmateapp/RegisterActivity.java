package travelmate.com.travelmateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import travelmate.com.travelmateapp.helpers.ProgressBarUpdater;
import travelmate.com.travelmateapp.tasks.RefreshTokenTask;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressBarUpdater progressBarUpdater;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView textViewSignin = findViewById(R.id.textViewSignIn);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        View progressCard = findViewById(R.id.registerProgressBar);
        progressBarUpdater = new ProgressBarUpdater(progressCard);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    public void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBarUpdater.updateProgress("Registering User...");
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBarUpdater.updateProgressVisibility();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                            new RefreshTokenTask(getApplicationContext()).execute(firebaseAuth.getCurrentUser().getUid(), refreshedToken);
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                registerUser();
                break;
            case R.id.textViewSignIn:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
    }
}
