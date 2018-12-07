package enib.gala;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
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

    private ListView mListViewConso;

    private View mProgressView;

    private boolean mScanned=false;

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
                    setSolde(21.23);
                }


            }
        });

        mProgressView = findViewById(R.id.loading_progress);
        showProgress(false);

        enableRecharge(false);

        //list
        mListViewConso=findViewById(R.id.listViewConso);


        Slidr.attach(this);


        //floating buttons
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //scan
                enableRecharge(false);
                mScanned=false;
                Intent i =  new Intent();
                i.setClass(getApplicationContext(), ScanBraceletActivity.class);
                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(),0,0);
                startActivityForResult(i,request_code_scan_bracelet, activityOptions.toBundle());
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //cancel
                enableRecharge(false);
                mScanned=false;
                //TODO
            }
        });

        FloatingActionButton fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //list
                enableAdd(false);
                enableList(true);
                //TODO
            }
        });

        FloatingActionButton fab4 = findViewById(R.id.fab4);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //add
                enableList(false);
                enableAdd(true);
                //TODO
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == request_code_scan_bracelet) {
            if (resultCode == RESULT_OK) {
                try
                {
                    String text=data.getData().toString();
                    afterBraceletCodeReturn(text);
                }
                catch (NullPointerException e)
                {
                    //TODO
                    e.getMessage();
                }


                //TODO
            }
        }
    }

    private void afterBraceletCodeReturn(String result)
    {
        //TODO
        mScanned=true;
        enableRecharge(true);
        setSolde(0.0);
    }

    private void setSolde(Double solde)
    {
        String text = solde.toString()+"€";
        mTextViewSolde.setText(text);
    }

    private void enableList(boolean enable)
    {


        if(enable)
        {
            if(mScanned)
            {
                List<Consumption> consumptionList = new ArrayList<>();
                consumptionList.add(new Consumption("ecocup", -1.0, 1));
                consumptionList.add(new Consumption("bouteille champagne", -22.0, 2));
                consumptionList.add(new Consumption("metre biere", -12.0, 2013,1,null,null,5.5));
                consumptionList.add(new Consumption("cb", 20.0, 2));
                consumptionList.add(new Consumption("vestiaire", -1.0, 2));
                consumptionList.add(new Consumption("preload", 20.0, 2));

                mListViewConso.setAdapter(new CustomListAdapter(this, consumptionList));

                mListViewConso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        Object o = mListViewConso.getItemAtPosition(position);
                        Consumption c = (Consumption) o;
                        Toast.makeText(getApplicationContext(), "Selected :" + " " + c, Toast.LENGTH_LONG).show();

                        AlertDialog alertDialog = new AlertDialog.Builder(Admin_RechargeMode.this).create();
                        alertDialog.setTitle("Info");
                        alertDialog.setMessage(c.toStringProper());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });

                mCardViewConsumptionList.setVisibility(View.VISIBLE);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Scan Fist", Toast.LENGTH_LONG).show();
            }


        }
        else
        {
            mCardViewConsumptionList.setVisibility(View.GONE);
        }
    }

    private void enableAdd(boolean enable)
    {

        if(enable)
        {
            if(mScanned)
            {
                mCardViewAddSolde.setVisibility(View.VISIBLE);
                mEditTextAddSolde.requestFocus();
                resetAddSoldeForm();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Scan Fist", Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            mCardViewAddSolde.setVisibility(View.GONE);
        }


    }


    private void enableRecharge(boolean enable)
    {
        if(enable)
        {
            mCardViewCurrentAccountSolde.setVisibility(View.VISIBLE);

            mTextViewRechargeInfo.setVisibility(View.GONE);
        }
        else
        {
            enableAdd(false);
            enableList(false);
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
