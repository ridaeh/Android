package cash.leeap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardInfo;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import java.util.Arrays;

import static cash.leeap.Data.ENABLE_GOOGLE_PAY;
import static cash.leeap.Data.PUBLISHABLE_KEY;

public class Payment extends AppCompatActivity {
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 12;
    final String TAG = "Payment";
    PaymentsClient paymentsClient;
    Button mButton_GooglePay;
    Button mButton_pay_card;
    CardMultilineWidget mCard_multiline_widget;
    EditText mEditText_name;
    Double value;
    Activity a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        String valueString = getIntent().getStringExtra("VALUE");
        value = Double.parseDouble(valueString);

        a = this;

        paymentsClient =
                Wallet.getPaymentsClient(this,
                        new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());

        TextView mTextView_valueToPay = findViewById(R.id.textView_valueToPay);
        valueString += "€";
        mTextView_valueToPay.setText(valueString);

        mButton_GooglePay = findViewById(R.id.button_GooglePay);
        mButton_GooglePay.setEnabled(false);
        mButton_GooglePay.setVisibility(ENABLE_GOOGLE_PAY ? View.VISIBLE : View.GONE);

        mCard_multiline_widget = findViewById(R.id.card_multiline_widget);
        mEditText_name = findViewById(R.id.editText_name);
        mEditText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText_name.getText().toString().isEmpty()) {
                    mEditText_name.setError(getString(R.string.prompt_enter_name));
                }
            }
        });

        mButton_pay_card = findViewById(R.id.button_pay_card);
        mButton_pay_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText_name.getText().toString().isEmpty()) {
                    mEditText_name.setError(getString(R.string.prompt_enter_name));
                    return;
                }

                Card cardToSave = mCard_multiline_widget.getCard();

                if (cardToSave != null) {
//                    Toast.makeText(a, "Card is not null", Toast.LENGTH_SHORT).show();
                    String name = mEditText_name.getText().toString();
                    cardToSave.setName(name);
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        isReadyToPay();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "onActivityResult: PAYMENT_DATA_REQUEST_CODE RESULT_OK");
                        try {
                            PaymentData paymentData = PaymentData.getFromIntent(data);
                            // You can get some data on the user's card, such as the brand and last 4 digits
                            CardInfo info = paymentData.getCardInfo();
                            UserAddress billingAddress = info.getBillingAddress();
//                            String name =billingAddress.getName();
//                            Log.i(TAG, "name : "+name);
                            // You can also pull the user address from the PaymentData object.
                            UserAddress address = paymentData.getShippingAddress();
                            Log.i(TAG, "address : " + address);
                            // This is the raw JSON string version of your Stripe token.
                            String rawToken = paymentData.getPaymentMethodToken().getToken();

                            // Now that you have a Stripe token object, charge that by using the id
                            Token stripeToken = Token.fromString(rawToken);
                            if (stripeToken != null) {
                                // This chargeToken function is a call to your own server, which should then connect
                                // to Stripe's API to finish the charge.
                                chargeToken(stripeToken.getId());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onActivityResult: ", e);
                        }


                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "onActivityResult: PAYMENT_DATA_REQUEST_CODE RESULT_CANCELED");
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);

                        try {

                            Log.i(TAG, "onActivityResult: result error" + status.getStatusMessage());
                        } catch (Exception e) {
                            Log.e(TAG, "onActivityResult: ", e);
                        }
                        // Log the status for debugging
                        // Generally there is no need to show an error to
                        // the user as the Google Payment API will do that
                        break;
                    default:
                        // Do nothing.
                }
                break; // Breaks the case LOAD_PAYMENT_DATA_REQUEST_CODE
            // Handle any other startActivityForResult calls you may have made.
            default:
                // Do nothing.
        }
    }

    private void chargeToken(String token) {

    }


    private void isReadyToPay() {
        IsReadyToPayRequest request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                .build();
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    public void onComplete(Task<Boolean> task) {
                        try {
                            boolean result;
                            try {
                                result = task.getResult(ApiException.class);
                            } catch (NullPointerException e) {
                                Log.e(TAG, "onComplete: task.getResult(ApiException.class)", e);
                                result = false;
                            }

                            if (result) {
                                //show Google as payment option
                                mButton_GooglePay.setEnabled(true);
                                mButton_GooglePay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PaymentDataRequest request = createPaymentDataRequest();
                                        if (request != null) {
                                            try {

                                                Task<PaymentData> t = paymentsClient.loadPaymentData(request);
                                                if (t == null) {
                                                    Log.e(TAG, "onClick: loadPaymentData null");
                                                }
                                                if (a == null) {
                                                    Log.e(TAG, "onClick: Activity null");
                                                }
                                                AutoResolveHelper.resolveTask(
                                                        t,
                                                        a,
                                                        LOAD_PAYMENT_DATA_REQUEST_CODE);
                                                // LOAD_PAYMENT_DATA_REQUEST_CODE is a constant integer of your choice,
                                                // similar to what you would use in startActivityForResult
                                            } catch (Exception e) {
                                                Log.e(TAG, "onClick Exception: AutoResolveHelper", e);
                                            }

                                        }
                                    }
                                });

                            } else {
                                mButton_GooglePay.setEnabled(false);
                                //hide Google as payment option
                            }
                        } catch (ApiException e) {
                            Log.e(TAG, "onComplete ApiException: " + e.getMessage(), e);
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onComplete NullPointerException: " + e.getMessage(), e);
                        } catch (Exception e) {
                            Log.e(TAG, "onComplete Exception: " + e.getMessage(), e);
                        }
                    }
                });
    }

    private PaymentMethodTokenizationParameters createTokenizationParameters() {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", "stripe")
                .addParameter("stripe:publishableKey", PUBLISHABLE_KEY)
                .addParameter("stripe:version", "2018-11-08")
                .build();
    }

    private PaymentDataRequest createPaymentDataRequest() {
        PaymentDataRequest.Builder request =
                PaymentDataRequest.newBuilder()
                        .setTransactionInfo(
                                TransactionInfo.newBuilder()
                                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                        .setTotalPrice(value.toString())
                                        .setCurrencyCode("EUR")
                                        .build())
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(Arrays.asList(
                                                WalletConstants.CARD_NETWORK_AMEX,
                                                WalletConstants.CARD_NETWORK_DISCOVER,
                                                WalletConstants.CARD_NETWORK_VISA,
                                                WalletConstants.CARD_NETWORK_MASTERCARD))
                                        .build());

        request.setPaymentMethodTokenizationParameters(createTokenizationParameters());
        return request.build();
    }


}
