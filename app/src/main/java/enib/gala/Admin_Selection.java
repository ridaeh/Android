package enib.gala;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Admin_Selection extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_selection);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
        }

    }

    public void goToBarMode(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Admin_BarMode.class);
        startActivity(intent);
        return;
    }

    public void goToEntranceMode(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Admin_EntranceMode.class);
        startActivity(intent);
        return;
    }

    public void goToRechargeMode(View view)
    {
        return;
    }
}
