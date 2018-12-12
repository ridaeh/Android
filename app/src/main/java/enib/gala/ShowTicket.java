package enib.gala;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.google.zxing.qrcode.QRCodeWriter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;

public class ShowTicket extends AppCompatActivity {

    ImageView QRCodeView;
    private View mProgressView;
    private View mTicketView;

    public int QRcodeWidth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ticket);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SlidrConfig config = new SlidrConfig.Builder().listener(new SlidrListener() {
            @Override
            public void onSlideStateChanged(int state) {

            }

            @Override
            public void onSlideChange(float percent) {

            }

            @Override
            public void onSlideOpened() {

            }

            @Override
            public void onSlideClosed() {
                returnData();
            }
        }).build();
        Slidr.attach(this,config);
//        toolbar

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Download", Snackbar.LENGTH_LONG).show();
            }
        });
        mProgressView = findViewById(R.id.loading_progress);
        mTicketView = findViewById(R.id.app_bar);

        mTicketView.setMinimumHeight(height);
        QRCodeView = findViewById(R.id.QRCode_image_view);
        showProgress(true);
        createQRCode("2qwe6r7t8973gfue45ygfo4e");
        showProgress(false);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        QRCodeView.setVisibility(show ? View.GONE : View.VISIBLE);
        QRCodeView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                QRCodeView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void returnData()
    {
        Intent data = new Intent();
        data.setData(Uri.parse("null")); //TODO can be optimized
        setResult(RESULT_OK, data);
        finish();
    }

    void createQRCode(final String text)
    {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.QRCodeBlackColor) : getResources().getColor(R.color.QRCodeWhiteColor));
                }
            }
            QRCodeView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


//    public class Ticket extends AsyncTask<String, Void, String> {
//
//        protected void onPreExecute()
//        {
//            super.onPreExecute();
//        }
//
//        protected String doInBackground(String... args) {
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPreExecute();
//        }
//    }

}