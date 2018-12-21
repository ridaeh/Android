package enib.gala;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

public class Admin_Selection extends AppCompatActivity {
    private UserAuth mAuth;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_selection);

        mAuth = new UserAuth(getApplicationContext());
        Slidr.attach(this);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Admin");
        adb.setMessage("You enter an administrator space, please leave if you are not");

        adb.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        adb.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        adb.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mUser = mAuth.getCurrentUser();
        if(mUser!=null)
        {

            mAuth.getAllInfo(mAuth.getCurrentUser()).getAllInfoListener(new UserAuth.GetAllInfoListener() {
                @Override
                public void GetAllInfoComplete(User u) {
                    if (u!=null)
                    {
                        Log.i("getAllInfo getAllInfoListener return", u.toString());
                        mUser=u;
                        if (!u.isAdmin()) {
                            finish();
                        }
                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"no user found", Toast.LENGTH_LONG).show();
            Log.i("getCurrentUser : ", "no user found");
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
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
        Intent intent = new Intent(getApplicationContext(), Admin_RechargeMode.class);
        startActivity(intent);
        return;
    }
}
