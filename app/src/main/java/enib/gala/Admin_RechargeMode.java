package enib.gala;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

public class Admin_RechargeMode extends AppCompatActivity {

    int request_code_scan_bracelet=24; //qrcode scanner

    private CardView mCardViewCurrentAccountSolde;
    private CardView mCardViewAddSolde;
    private TextView mTextViewRechargeInfo;
    private TextView mTextViewSolde;
    private TextView mEditTextAddSolde;
    private ImageButton mImageButtonAdd;
    private RadioGroup mRadioGroupPayment;

    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__recharge_mode);

        //init view
        Toolbar toolbar = findViewById(R.id.toolbar); setSupportActionBar(toolbar);
        mCardViewCurrentAccountSolde = findViewById(R.id.cardViewCurrentAccountSolde);
        mCardViewAddSolde=findViewById(R.id.cardViewAddSolde);

        mTextViewRechargeInfo=findViewById(R.id.textViewRechargeInfo);
        mTextViewSolde=findViewById(R.id.editTextSolde);
        mTextViewSolde.setKeyListener(null);
        mImageButtonAdd=findViewById(R.id.imageButtonAdd);
        mEditTextAddSolde=findViewById(R.id.editTextAddSolde);
        mRadioGroupPayment=findViewById(R.id.radioGroupPayment);
        mImageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mEditTextAddSolde.getText().toString();
                if(s.isEmpty())
                {
                    mEditTextAddSolde.setError("empty");
                    return;
                }
                Float f= Float.parseFloat(s);
                if(f.isNaN()||f<=0)
                {
                    mEditTextAddSolde.setError("error");
                    return;
                }

                switch (mRadioGroupPayment.getCheckedRadioButtonId())
                {
                    case R.id.radioButtonCash : break;
                    case R.id.radioButtonCB : break;
                    default: mEditTextAddSolde.setError("select payment method"); Toast.makeText(getApplicationContext(),"select payment method", Toast.LENGTH_LONG).show(); return;
                }
                mEditTextAddSolde.setError(null);

                Toast.makeText(getApplicationContext(),"all is good", Toast.LENGTH_LONG).show();

                showProgress(true);
                //TODO


            }
        });

        mProgressView = findViewById(R.id.loading_progress);
        showProgress(false);

        enableRecharge(false);

        Slidr.attach(this);


        //floating butons
        FloatingActionButton fab = findViewById(R.id.fab);
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

        FloatingActionButton fab2 = findViewById(R.id.fab2);
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
        mTextViewSolde.setText("0.0");
    }

    private void enableRecharge(boolean enable)
    {
        if(enable)
        {
            mCardViewAddSolde.setVisibility(View.VISIBLE);
            mCardViewCurrentAccountSolde.setVisibility(View.VISIBLE);
            mCardViewCurrentAccountSolde.requestFocus();
            mTextViewRechargeInfo.setVisibility(View.GONE);
        }
        else
        {
            mCardViewAddSolde.setVisibility(View.GONE);
            mCardViewCurrentAccountSolde.setVisibility(View.GONE);
            mTextViewRechargeInfo.setVisibility(View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCardViewAddSolde.setEnabled(!show);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }
}
