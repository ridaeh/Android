package cash.leeap;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;

import static cash.leeap.Data.MIN_RELOAD_VALUE;

public class Reload extends AppCompatActivity {
    TextView mTextView_seekBar_value;
    SeekBar mSeekBar;
    TabHost host;
    int shift = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Slidr.attach(this);
        setContentView(R.layout.activity_reload);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = getReloadValue();
                if (value != null) {
                    Snackbar.make(view, "Value you wan to add : " + value.toString(), Snackbar.LENGTH_LONG).show();
                    //TODO pay
                    Intent intent = new Intent(getBaseContext(), Payment.class);
                    intent.putExtra("VALUE", value.toString());
                    startActivity(intent);
                }
            }
        });

        host = findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        String tab1 = "Pick";

        TabHost.TabSpec spec = host.newTabSpec(tab1);
        spec.setContent(R.id.tab1);
        spec.setIndicator(tab1);
        host.addTab(spec);

        //Tab 2
        String tab2 = "Choose";

        spec = host.newTabSpec(tab2);
        spec.setContent(R.id.tab2);
        spec.setIndicator(tab2);
        host.addTab(spec);

        mTextView_seekBar_value = findViewById(R.id.textView_seekBar_value);
        String text = MIN_RELOAD_VALUE.toString() + "€";
        mTextView_seekBar_value.setText(text);

        mSeekBar = findViewById(R.id.seekBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSeekBar.setMin(MIN_RELOAD_VALUE);
            mSeekBar.setMax(100);
            shift = 0;
        } else {
            shift = MIN_RELOAD_VALUE;
            mSeekBar.setMax(100 - MIN_RELOAD_VALUE);
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Integer progressInteger = progress + shift;
                String text = progressInteger.toString() + "€";
                mTextView_seekBar_value.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    Integer getReloadValue() {

        if (host.getCurrentTab() == 0) {
            //Pick
            return mSeekBar.getProgress() + shift;
        } else {
            EditText mEditText_reloadValue = findViewById(R.id.editText_reloadValue);
            String value = mEditText_reloadValue.getText().toString();
            if (value.isEmpty()) {
                mEditText_reloadValue.setError("error");
                return null;
            }
            int reloadValue = Integer.parseInt(value);
            if (reloadValue < MIN_RELOAD_VALUE) {
                mEditText_reloadValue.setError("Must be more than " + MIN_RELOAD_VALUE.toString());
                return null;
            }
            return reloadValue;
        }
    }


}
