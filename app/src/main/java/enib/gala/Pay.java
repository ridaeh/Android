package enib.gala;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Pay {

    private PaymentDoneCompleteListener listenerPaymentDone;
    public Pay() {
        this.listenerPaymentDone = null;
    }

    public interface PaymentDoneCompleteListener {
        void PaymentDoneComplete(boolean success,String text);
    }

    public void setPayementDoneCompleteListener(PaymentDoneCompleteListener listener) {
        this.listenerPaymentDone = listener;
    }

    public Pay paymentListProduct(List<Product> productsList,String token, Integer userId)
    {
        JSONArray data=new JSONArray();
        for(int i=0;i<productsList.size(); i++)
        {
            try {
                JSONObject j = new JSONObject();
                j.put("qt", productsList.get(i).getCount());
                j.put("id", productsList.get(i).getId()); //conso id
                data.put(j);
            }
            catch (Exception e)
            {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
        try {
            Log.i("data",data.toString());
        }
        catch (Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
        }


        PayPost p = new PayPost();
        p.setData(data);
        p.setToken(token);
        p.setUserId(userId);
        p.execute();
        return this;
    }

    public class PayPost extends AsyncTask<String, Void, String> {
        private JSONArray data;
        private Integer userId;
        private String token;

        protected void setData(JSONArray d)
        {
            this.data=d;
        }

        protected void setUserId(Integer userId)
        {
            this.userId=userId;
        }

        public void setToken(String token) {
            this.token = token;
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            try {
                URL url = new URL(Data.getApiUrl()); // here is your URL path
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setReadTimeout(7000);
                con.setConnectTimeout(7000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("POST");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userId", userId);
                jsonObject.accumulate("token", token);
                jsonObject.put("conso", data);

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("pay",jsonObject);
                JSONObject jsonObject3 = new JSONObject();
                jsonObject3.accumulate("consommations",jsonObject2);
                String param =jsonObject3.toString();

                Log.i("param",param);

                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print("stringified=");
                out.print(param);
                out.close();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    con.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("PayPost result",result);
            try {
                JSONObject obj = new JSONObject(result);
                boolean success = false;
                try
                {
                    success=(boolean) obj.get("success");
                    listenerPaymentDone.PaymentDoneComplete(success, (String) obj.get("text"));
                }
                catch(Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    listenerPaymentDone.PaymentDoneComplete(success, "return error");
                }


                Log.d("PayPost", obj.toString());

            } catch (Throwable t) {
                listenerPaymentDone.PaymentDoneComplete(false,"internal error");
                Log.e("PayPost", "Could not parse malformed JSON: \"" + result + "\"");

            }
        }
    }
}
