package enib.gala;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

public class ScanBraceletActivity extends AppCompatActivity {

    private Button buttonAction;
    private String braceletValue;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bracelet);
        buttonAction=findViewById(R.id.button_scan_bracelet_action);
        braceletValue="salut";

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SlidrConfig config = new SlidrConfig.Builder()
                                .position(SlidrPosition.TOP)

                                .build();

        Slidr.attach(this, config);


        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NFC is enable.", Toast.LENGTH_LONG).show();
        }

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        // TODO: handle Intent
    }

    private void returnData()
    {
        Intent data = new Intent();
        data.setData(Uri.parse(braceletValue));
        setResult(RESULT_OK, data);
        finish();
    }
}
