package enib.gala;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Admin_RechargeMode extends AppCompatActivity {

    int request_code_scan_bracelet=24; //qrcode scanner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__recharge_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent();
                i.setClass(getApplicationContext(), ScanBraceletActivity.class);
                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(),0,0);
                startActivityForResult(i,request_code_scan_bracelet, activityOptions.toBundle());
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == request_code_scan_bracelet) {
            if (resultCode == RESULT_OK) {
                afterBraceletCodeReturn(data.getData().toString());
                //TODO
            }
        }
    }

    private void afterBraceletCodeReturn(String result)
    {

    }

}
