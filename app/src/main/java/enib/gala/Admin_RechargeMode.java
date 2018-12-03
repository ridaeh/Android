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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

public class Admin_RechargeMode extends AppCompatActivity {

    int request_code_scan_bracelet=24; //qrcode scanner

    private CardView mCardViewCurrentAccountSolde;
    private CardView mCardViewAddSolde;
    private CardView mCardViewConsumptionList;
    private TextView mTextViewRechargeInfo;
    private TextView mTextViewSolde;
    private TextView mEditTextAddSolde;
    private ImageButton mImageButtonAdd;

    private RadioGroup mRadioGroupPayment;
    private RadioButton mRadioButtonCash;
    private RadioButton mRadioButtonCB;

    private ListView mListViewConso;

    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__recharge_mode);

        //init view
        Toolbar toolbar = findViewById(R.id.toolbar); setSupportActionBar(toolbar);
        mCardViewCurrentAccountSolde = findViewById(R.id.cardViewCurrentAccountSolde);
        mCardViewAddSolde=findViewById(R.id.cardViewAddSolde);
        mCardViewConsumptionList=findViewById(R.id.cardViewConsumptionList);

        mTextViewRechargeInfo=findViewById(R.id.textViewRechargeInfo);
        mTextViewSolde=findViewById(R.id.editTextSolde);
        mTextViewSolde.setKeyListener(null);
        mImageButtonAdd=findViewById(R.id.imageButtonAdd);
        mEditTextAddSolde=findViewById(R.id.editTextAddSolde);

        //radio button
        mRadioGroupPayment=findViewById(R.id.radioGroupPayment);
        mRadioButtonCash=findViewById(R.id.radioButtonCash);
        mRadioButtonCash=findViewById(R.id.radioButtonCB);

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
                boolean success =true;
                if(success)
                {
                    //TODO maj account balance
                }


            }
        });

        mProgressView = findViewById(R.id.loading_progress);
        showProgress(false);

        enableRecharge(false);

        //list
        mListViewConso=findViewById(R.id.listViewConso);
        List<Consumption> consumptionList = new ArrayList<Consumption>();
        consumptionList.add(new Consumption("conso1", 1.1, 1));
        consumptionList.add(new Consumption("conso2", 2.2, 2));
        consumptionList.add(new Consumption("conso2", 2.2, 2));
        consumptionList.add(new Consumption("conso2", 2.2, 2));
        consumptionList.add(new Consumption("conso2", 2.2, 2));
        consumptionList.add(new Consumption("conso2", 2.2, 2));
        consumptionList.add(new Consumption("conso2", 2.2, 2));
        consumptionList.add(new Consumption("conso2", 2.2, 2));
        consumptionList.add(new Consumption("conso2", 2.2, 2));

        mListViewConso.setAdapter(new CustomListAdapter(this, consumptionList));

        Slidr.attach(this);


        //floating buttons
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
            mCardViewConsumptionList.setVisibility(View.VISIBLE);
            mCardViewCurrentAccountSolde.setVisibility(View.VISIBLE);
            mCardViewCurrentAccountSolde.requestFocus();
            mTextViewRechargeInfo.setVisibility(View.GONE);
        }
        else
        {
            mCardViewAddSolde.setVisibility(View.GONE);
            mCardViewConsumptionList.setVisibility(View.GONE);
            mCardViewCurrentAccountSolde.setVisibility(View.GONE);
            mTextViewRechargeInfo.setVisibility(View.VISIBLE);
            resetAddSoldeForm();
        }
    }

    private void resetAddSoldeForm()
    {
        showProgress(false);
        mEditTextAddSolde.setText("");
        mRadioGroupPayment.clearCheck();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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
    }
}
