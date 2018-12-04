package enib.gala;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.r0adkll.slidr.Slidr;


public class Admin_EntranceMode extends AppCompatActivity {

    private ProgressBar mProgressBarHomeUserChecked;
    private TextView mTextViewHomeUserChecked;

    private TextView mTextViewQRCodeShow;
    private TextView mTextViewLastName;
    private TextView mTextViewFistName;
    private TextView mTextViewEmail;
    private TextView mTextViewAccountValue;
    private CardView mCardViewPlace;
    private Boolean mPlaceWorks=false;

    private TextView mTextViewBraceletCode;
    private Button mButtonBind;
    private String mBraceletCode;

    private ViewFlipper mView;

    //define a request code to know witch activity return smth
    int request_code_scan_qrcode=12; //qrcode scanner
    int request_code_scan_bracelet=24; //qrcode scanner

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setupHome();
                    mView.setDisplayedChild(0);
                    return true;
                case R.id.navigation_scan_qrcode:
                    resetPlaceCard();
                    Intent intent = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                    startActivityForResult(intent,request_code_scan_qrcode);
                    mView.setDisplayedChild(1);

                    return true;
                case R.id.navigation_scan_bracelet:
                    if(mPlaceWorks)
                    {
                        //lunch scan bracelet activity
                        Intent i =  new Intent();
                        i.setClass(getApplicationContext(), ScanBraceletActivity.class);
                        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(),0,0);
                        startActivityForResult(i,request_code_scan_bracelet, activityOptions.toBundle());

                        //init display
                        mButtonBind.setEnabled(false);
                        mTextViewBraceletCode.setText("");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Place Problem", Toast.LENGTH_LONG).show();
                    }



                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__entrance_mode);

        mView=findViewById(R.id.vf);

        initView();
        setupHome();

        Slidr.attach(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setupHome()
    {
        //TODO
        Integer userChecked=366;
        Integer userTotal=666;
        Float percent= (userChecked.floatValue()/userTotal.floatValue())*100;
        mProgressBarHomeUserChecked.setProgress(percent.intValue());
        String text=userChecked.toString()+"/"+userTotal.toString();
        mTextViewHomeUserChecked.setText(text);
    }


    private void initView()
    {
        //home
        mProgressBarHomeUserChecked=findViewById(R.id.progressBarHomeUserChecked);
        mTextViewHomeUserChecked=findViewById(R.id.textViewHomeUserChecked);

        //qrcode
        mTextViewQRCodeShow=findViewById(R.id.textView_qr_code_value);
        mCardViewPlace=findViewById(R.id.card_view_place);
        mTextViewLastName=findViewById(R.id.textView_last_name_value);
        mTextViewFistName=findViewById(R.id.textView_first_name_value);
        mTextViewEmail=findViewById(R.id.textView_email_value);
        mTextViewAccountValue=findViewById(R.id.textView_account_value);

        //bracelet
        mTextViewBraceletCode=findViewById(R.id.textViewBraceletCode);
        mButtonBind=findViewById(R.id.buttonBind);

        mButtonBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //TODO
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code_scan_qrcode) {
            if (resultCode == RESULT_OK) {
                afterQRCodeReturn(data.getData().toString());
                //TODO
            }
        }
        else if (requestCode == request_code_scan_bracelet) {
            if (resultCode == RESULT_OK) {
                afterBraceletCodeReturn(data.getData().toString());
                //TODO
            }
        }
    }

    public void afterBraceletCodeReturn(String result)
    {
        mView.setDisplayedChild(2);
        mBraceletCode=result;
        boolean achiveRequest=true;//TODO
        if(achiveRequest)
        {
            mButtonBind.setEnabled(true);
            mTextViewBraceletCode.setText(mBraceletCode);
            //TODO
        }

    }

    public void afterQRCodeReturn(String result)
    {

        if(result.isEmpty())
        {

            mCardViewPlace.setCardBackgroundColor(Color.RED);
            mPlaceWorks=false;
        }
        else
        {
            //TODO : ask to server


            String email="email";
            String last_name="last_name";
            String first_name="first_name";
            Double account=0.0;
            boolean achiveRequest=true;
            boolean alreadyChecked=true;
            mPlaceWorks=achiveRequest&&alreadyChecked;

            //update view
            if(achiveRequest)
            {
                mCardViewPlace.setCardBackgroundColor(Color.GREEN);
                setPlaceCardInfo(result,email,last_name,first_name,account);
                if(!alreadyChecked)
                {

                }
                else
                {

                }
//                Snackbar.make(mView.getCurrentView(), "Now you can bind bracelet", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                }).show();
            }
            else
            {
                //TODO : add warning
                mCardViewPlace.setCardBackgroundColor(Color.RED);
            }



        }

    }

    private void setPlaceCardInfo(String code, String email, String last_name, String first_name, Double account)
    {
        mTextViewQRCodeShow.setText(code);
        mTextViewLastName.setText(last_name);
        mTextViewFistName.setText(first_name);
        mTextViewEmail.setText(email);
        mTextViewAccountValue.setText(account.toString()+"€");
    }

    private void resetPlaceCard()
    {
        mTextViewQRCodeShow.setText("");
        mTextViewLastName.setText("");
        mTextViewFistName.setText("");
        mTextViewEmail.setText("");
        mTextViewAccountValue.setText("");
        mCardViewPlace.setCardBackgroundColor(Color.WHITE);
    }

}
