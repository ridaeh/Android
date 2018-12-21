package enib.gala;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UserAuth {
    private MyDBHandler db;

    private SignInCompleteListener listenerSignIn;
    private SignUpCompleteListener listenerSignUp;
    private GetAllInfoListener listenerGetAllInfo;
    private Context mContext;

//    private String mToken;
    private String mPassword;
    private String mEmail;
    private String mToken;
    private Integer mId;

    public UserAuth(Context context)
    {
        this.listenerSignIn = null;
        this.listenerSignUp = null;
        this.listenerGetAllInfo = null;
        this.mContext=context;
    }

    public User getCurrentUser()
    {
        db = new MyDBHandler(mContext, null, null, 1);
        User u = db.findFirstHandler();
        if(u.getEmail()==null || u.getPassword()==null || u.getId()==null || u.getToken()==null)
        {
            return null;
        }
        else
        {
            return u;
        }
    }

    public void signOut()
    {
        db = new MyDBHandler(mContext, null, null, 1);
        db.emptyTable();
        db.close();
    }

    public UserAuth signIn(String email, String password)
    {
        mPassword=password;
        mEmail=email;
        new ConnectionPost().execute(email,password);
        return this;
    }

    public UserAuth signUp(String firstname, String email, String password, String phone)
    {
        mPassword=password;
        mEmail=email;
        new InscriptionPost().execute(firstname,email,password,phone);
        return this;
    }

    public UserAuth getAllInfo(User u)
    {
        if(u!=null)
        {
            Log.i("getAllInfo", "user not empty");
            mPassword=u.getPassword();
            mEmail=u.getEmail();
            mToken=u.getToken();
            mId=u.getId();
            new ConnectionInfoPost().execute(mToken);
        }
        else
        {
            Log.e("getAllInfo", "user null");
            listenerGetAllInfo.GetAllInfoComplete(null);
        }

        return this;
    }

    public interface GetAllInfoListener {
        void GetAllInfoComplete(User u);
    }

    public interface SignUpCompleteListener {
        void SignUpComplete(boolean success);
    }

    public interface SignInCompleteListener {
        void SignInComplete(boolean success);
    }

    public void setSignInCompleteListener(SignInCompleteListener listener) {
        this.listenerSignIn = listener;
    }

    public void setSignUpCompleteListener(SignUpCompleteListener listener) {
        this.listenerSignUp = listener;
    }

    public void getAllInfoListener(GetAllInfoListener listener)
    {
        this.listenerGetAllInfo=listener;
    }

    public class ConnectionInfoPost extends AsyncTask<String, Void, String> {

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
                jsonObject.accumulate("getLogedUserProfile", true);
                jsonObject.accumulate("token", args[0]);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("profile",jsonObject);
                String param =jsonObject2.toString();

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
            Log.i("ConnectionPostInfo result",result);
            try {
                JSONObject obj = new JSONObject(result);
//                JSONArray array = new JSONArray(obj);
                if((boolean) obj.get("success"))
                {
                    Log.i("getAllInfo onPostExecute", "success");
                    String sAdmin =null;
                    String sBalance = (String) obj.get("Balance");
                    Double balance=null;
                    try
                    {
                        balance = Double.parseDouble(sBalance);
                        balance=balance/100;
                        Log.i("getAllInfo onPostExecute balance : ", balance.toString());
                    }
                    catch (Exception e)
                    {
                        Log.e("getAllInfo onPostExecute parseDouble", e.toString());
                    }

                    try
                    {
                        sAdmin = (String) obj.get("Administrator");
                        Log.i("getAllInfo onPostExecute balance : ", sAdmin);
                    }
                    catch (Exception e)
                    {
                        Log.e("getAllInfo onPostExecute getadmin", e.toString());
                    }
                    try
                    {
                        listenerGetAllInfo.GetAllInfoComplete(new User(mId, (String) obj.get("Firstname"), (String) obj.get("Lastname"), (String) obj.get("Email"), (String) obj.get("Phone"), (String) obj.get("PhoneIndicative"), (String) obj.get("City"), (String) obj.get("Postcode"), (String) obj.get("Address"), mPassword, balance, mToken, sAdmin, null, (String) obj.get("QRCode")));
                    }
                    catch (Exception e)
                    {
                        Log.e("getAllInfo onPostExecute return listener", e.toString());
                    }



                }
                else
                {
                    Log.e("getAllInfo onPostExecute", "error");
                    //TODO
                }

                listenerGetAllInfo.GetAllInfoComplete(null);

            } catch (Throwable t) {
                Log.e("getAllInfo", "Could not parse malformed JSON: \"" + result + "\"");
                listenerGetAllInfo.GetAllInfoComplete(null);

            }

        }

    }
    public class ConnectionPost extends AsyncTask<String, Void, String> {

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
                jsonObject.accumulate("identifier", args[0]);
                jsonObject.accumulate("password", args[1]);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("login",jsonObject);
                String param =jsonObject2.toString();

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
            Log.i("ConnectionPost result",result);
            try {

                JSONObject obj = new JSONObject(result);

                if((boolean) obj.get("success"))
                {

                    String token= (String) obj.get("token");
                    Integer id =Integer.parseInt((String) obj.get("id"));
                    MyDBHandler db = new MyDBHandler(mContext, null, null, 1);
                    db.emptyTable();

                    User u =new User(id,mEmail,mPassword,token);
                    db.addHandler(u);
                    db.close();
                    listenerSignIn.SignInComplete(true);
                }
                else
                {
                    //TODO
                    listenerSignIn.SignInComplete(false);
                }

                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
                listenerSignIn.SignInComplete(false);
            }
        }
    }
    public class InscriptionPost extends AsyncTask<String, Void, String> {

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
                jsonObject.accumulate("firstname", args[0]);
                jsonObject.accumulate("email", args[1]);
                jsonObject.accumulate("password", args[2]);
                jsonObject.accumulate("phone", args[3]);

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("signup",jsonObject);
                String param =jsonObject2.toString();

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
            Log.i("ConnectionPost result",result);
            try {
                JSONObject obj = new JSONObject(result);

                if((boolean) obj.get("success"))
                {

                    listenerSignUp.SignUpComplete(true);
                }
                else
                {
                    //TODO
                    listenerSignUp.SignUpComplete(false);
                }

                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
                listenerSignUp.SignUpComplete(false);
            }
        }
    }



}
