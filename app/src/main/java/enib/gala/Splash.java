package enib.gala;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Splash extends AppCompatActivity {
    private User mUser;
    private UserAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = new UserAuth(getApplicationContext());
        mUser = mAuth.getCurrentUser();
        final Intent intent;
        if(mUser!=null)
        {
           intent = new Intent(this, Main.class);
        }
        else{
            intent = new Intent(this, Login.class);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                    // Try to sleep the thread for 20 milliseconds
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                startActivity(intent);
                finish();

            }
        }).start(); // Start the operation

    }
}

