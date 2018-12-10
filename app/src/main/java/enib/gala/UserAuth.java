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
    private static final String API_URL = "https://api.leeap.cash/";


    private SignInCompleteListener listenerSignIn;
    private SignUpCompleteListener listenerSignUp;
    private Context mContext;

//    private String mToken;
    private String mPassword;
    private String mEmail;

    public UserAuth(Context context)
    {
        this.listenerSignIn = null;
        this.listenerSignUp = null;
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

    public void signUp(String firstname, String email, String password, String phone)
    {
        //TODO
    }

    public interface SignInCompleteListener {
        void SignInComplete(boolean success);

    }

    public interface SignUpCompleteListener {
        void SignUpComplete(boolean success);

    }

    public void setSignInCompleteListener(SignInCompleteListener listener) {
        this.listenerSignIn = listener;
    }

    public void setSignUpCompleteListener(SignUpCompleteListener listener) {
        this.listenerSignUp = listener;
    }

    public class ConnectionInfoPost extends AsyncTask<String, Void, String> {

        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            try {
                URL url = new URL(API_URL); // here is your URL path
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
                    String token = (String) obj.get("token");
                }
                else
                {
                    //TODO
                }

                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");

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
                URL url = new URL(API_URL); // here is your URL path
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



}
