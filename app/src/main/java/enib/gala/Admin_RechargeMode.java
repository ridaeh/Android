package enib.gala;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Admin_RechargeMode extends AppCompatActivity {

    int request_code_scan_bracelet=24; //qrcode scanner

    private CardView mCardViewCurrentAccountSolde;
    private CardView mCardViewAddSolde;
    private TextView mTextViewRechargeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__recharge_mode);

        //init view
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); setSupportActionBar(toolbar);
        mCardViewCurrentAccountSolde = findViewById(R.id.cardViewCurrentAccountSolde);
        mCardViewAddSolde=findViewById(R.id.cardViewAddSolde);
        mTextViewRechargeInfo=findViewById(R.id.textViewRechargeInfo);

        enableRecharge(false);


        //floating butons
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableRecharge(false);
                Intent i =  new Intent();
                i.setClass(getApplicationContext(), ScanBraceletActivity.class);
                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(),0,0);
                startActivityForResult(i,request_code_scan_bracelet, activityOptions.toBundle());
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableRecharge(false);
                //TODO
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
        //TODO
        enableRecharge(true);
    }

    private void enableRecharge(boolean enable)
    {
        if(enable)
        {
            mCardViewAddSolde.setVisibility(View.VISIBLE);
            mCardViewCurrentAccountSolde.setVisibility(View.VISIBLE);
            mTextViewRechargeInfo.setVisibility(View.GONE);
        }
        else
        {
            mCardViewAddSolde.setVisibility(View.GONE);
            mCardViewCurrentAccountSolde.setVisibility(View.GONE);
            mTextViewRechargeInfo.setVisibility(View.VISIBLE);
        }

    }

}
