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
    private EditText email;
    private EditText password;
    private EditText mEditTextPhoneNumber;
    private EditText mEditTextFirstName;
    private TextView status;

    private UserAuth mAuth;
    private User mUser;

    private static final String TAG = "createAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email =  findViewById(R.id.editTextEmail);
        password =  findViewById(R.id.editTextPassword);
        status = findViewById(R.id.textViewStatut);
        mEditTextPhoneNumber=findViewById(R.id.editViewPhoneNumber);
        mEditTextFirstName=findViewById(R.id.editViewFirstName);

        email.requestFocus();

        mAuth = new UserAuth(getApplicationContext());

        Log.d(TAG, "onCreate");

    }
    @Override
    public void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if(mUser!=null)
        {
            //TODO
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
            finish();
        }
        else
        {
            //TODO
        }
    }

    public void register(View view)
    {
        status.setText("");
        email.setError(null);
        password.setError(null);
        String email_ =email.getText().toString();
        String password_ =password.getText().toString();
        String phoneNumber =mEditTextPhoneNumber.getText().toString();

        String firstName =mEditTextFirstName.getText().toString();

        Switch conditionAccept = findViewById(R.id.switch_accept_condition);

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
        if (phoneNumber.isEmpty() || firstName.isEmpty())
        {
            Log.d(TAG, "createUserWithEmail:phoneNumber or fistName empty");
            return;
        }

        mAuth.signUp(firstName,email_,password_,phoneNumber).setSignUpCompleteListener(new UserAuth.SignUpCompleteListener() {
            @Override
            public void SignUpComplete(boolean success) {
                if(success)
                {
                    Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_LONG).show();
                }
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
