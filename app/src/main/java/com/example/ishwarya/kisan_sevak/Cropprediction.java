package com.example.ishwarya.kisan_sevak;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Cropprediction extends AppCompatActivity {


    int status;
    int statepos ;
    int seasonpos ;
    String district;
    ArrayList<String> croplist = new ArrayList<>();
    ProgressBar pgrbar;
    TextView t1, t2, t3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropprediction);

        Bundle bundle = getIntent().getExtras();
        statepos = bundle.getInt("state_id");
        seasonpos = bundle.getInt("season_id");
        district = bundle.getString("district");

        pgrbar = (ProgressBar)findViewById(R.id.progress_bar);

        t1 = (TextView)findViewById(R.id.crop1);
        t2 = (TextView)findViewById(R.id.crop2);
        t3 = (TextView)findViewById(R.id.crop3);

        pgrbar.setVisibility(View.VISIBLE);


        GetPredictedCrop gpc = new GetPredictedCrop();
        gpc.execute();
    }


    public class GetPredictedCrop extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            String inpurl = "https://kisansevakproject.appspot.com/?state="+String.valueOf(statepos)+"&district="+district+"&season="+String.valueOf(seasonpos);


            try {
                URL url = new URL(inpurl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);

                status = urlConnection.getResponseCode();

                System.out.println("Hello"); //just a debugging statement
                System.out.println("Status: " + status);


                if (status == 200 || status == 201) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    Log.v("br", "after br");
                    StringBuilder sb = new StringBuilder();
                    Log.v("sb", "after sb");
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    String jsonString = sb.toString();

                    JSONObject jsonObject = new JSONObject(jsonString);

                    JSONArray crops = jsonObject.getJSONArray("result");

                    for(int i=0; i<crops.length();i++){

                        String st = crops.getString(i);
                        croplist.add(st);
                    }





                }


            } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }




            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pgrbar.setVisibility(View.INVISIBLE);
            t1.setText(croplist.get(0));
            t2.setText(croplist.get(1));
            t3.setText(croplist.get(2));



        }


    }

}


