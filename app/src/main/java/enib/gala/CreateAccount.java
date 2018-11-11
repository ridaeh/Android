package enib.gala;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText email;
    private EditText password;
    private TextView status;

    private static final String TAG = "createAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        email =  findViewById(R.id.editTextEmail);
        password =  findViewById(R.id.editTextPassword);
        status = findViewById(R.id.textViewStatut);
        email.requestFocus();
        Log.d(TAG, "onCreate");

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser !=null )
        {
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
        }
    }

    public void register(View view)
    {
        status.setText("");
        email.setError(null);
        password.setError(null);
        String email_ =email.getText().toString();
        String password_ =password.getText().toString();
        Switch conditionAccept = (Switch) findViewById(R.id.switch_accept_condition);

        if (!conditionAccept.isChecked())
        {
            Toast.makeText(getApplicationContext(), "Please accept condition.",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "createUserWithEmail:conditionNotAccepted");
            return;
        }

        if (!paramsValid(email_,password_))
        {
            Log.d(TAG, "createUserWithEmail:invalidParams");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email_,password_ )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            status.setText("success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            finish();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            status.setText("failure");
//                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private boolean paramsValid(String email_,String password_)
    {
        boolean error=false;
        if (!email_.contains("@"))
        {
            email.setError("must contains @");
            error=true;
        }
        else if (!(password_.length()>4))
        {
            password.setError("too short");
            error=true;
        }

        return !error;

    }
}
