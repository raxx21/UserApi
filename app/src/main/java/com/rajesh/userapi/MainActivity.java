package com.rajesh.userapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aquery.AQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
//  Tmp Hashmap for storing the data
    HashMap<String,String> result = new HashMap<>();
    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new getUser().execute();
        aq= new AQuery(this);
    }

    private class getUser extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Pre","Started the progress");
            Toast.makeText(MainActivity.this,"User details are loading...",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
//            from HttpHandle class to take a response from server
            HttpHandler sh = new HttpHandler();
//          making a request to the url and getting response
            String url = "https://randomuser.me/api/";
            String jsonStr = sh.makeServiceCall(url);

            Log.d("jsonStr response : ", jsonStr);
            if(jsonStr != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);
//                    Getting json array node
                    JSONArray results = jsonObject.getJSONArray("results");
//                    looping through results
                    for (int i=0; i<results.length();i++){
                        JSONObject c = results.getJSONObject(i);
                        String gender = c.getString("gender");
                        String email = c.getString("email");
                        String phone = c.getString("phone");
//                      name its an json object
                        JSONObject name = c.getJSONObject("name");
                        String first = name.getString("first");
                        String last = name.getString("last");
//                      address its an json object
                        JSONObject location = c.getJSONObject("location");
                        String city = location.getString("city");
                        String state = location.getString("state");
                        //Image its an json object
                        JSONObject picture = c.getJSONObject("picture");
                        String img = picture.getString("medium");
//                      Adding each child node to hashmap
                        result.put("first",first);
                        result.put("last",last);
                        result.put("email",email);
                        result.put("gender",gender);
                        result.put("phone",phone);
                        result.put("city",city);
                        result.put("state",state);
                        result.put("img",img);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Error","Json parsing error");
                } ;
            }
            else {
                Log.d("Error","Couldn't get json from server");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // intsialze aquery
            Log.d("Post","ended");
            EditText user_fullName = findViewById(R.id.full_name);
            EditText user_email = findViewById(R.id.email);
            EditText user_gender = findViewById(R.id.gender);
            EditText user_phone = findViewById(R.id.phone);
            EditText user_address = findViewById(R.id.add);
            user_fullName.setText(result.get("first") + " " +result.get("last"));
            user_email.setText(result.get("email"));
            user_gender.setText(result.get("gender"));
            user_phone.setText(result.get("phone"));
            user_address.setText(result.get("city") + "," +result.get("state"));
            aq.id(R.id.pic).image(result.get("img"));
            Toast.makeText(MainActivity.this,"User details completed",Toast.LENGTH_SHORT).show();
        }
    }
}
